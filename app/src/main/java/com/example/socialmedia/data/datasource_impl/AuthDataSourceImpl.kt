package com.example.socialmedia.data.datasource_impl

import android.content.Context
import android.credentials.GetCredentialException
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.socialmedia.data.datasource.AuthDatasource
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.supabase.SupabaseClientModule
import com.example.socialmedia.domain.supabase.SupabaseClientModule.SUPABASE_SERVER_CLIENT_ID
import com.example.socialmedia.utils.PasswordUtils
import com.example.socialmedia.utils.TokenManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID

private val TAG = "AuthDataSourceImpl"

@Serializable
private data class User(
    val username: String,
    val email: String,
    @SerialName("id")
    val userid: String
)

@Serializable
private data class UserEmail(
    val email: String
)

class AuthException(message: String) : Exception(message)

class AuthDataSourceImpl(
    private val supabase: SupabaseClient
) : AuthDatasource {
    override suspend fun register(
        email: String,
        password: String,
        username: String
    ): Boolean {
        
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            val currentUser = supabase.auth.currentUserOrNull()
            
            val userInfo = currentUser
                ?: throw Exception("Something went wrong. Please try again!")
            
            val createdAt = userInfo.createdAt
            
            val userId = userInfo.id
            
            val hashPassword = PasswordUtils.hashPassword(password)
            
            val userJwt = User(
                username,
                email,
                userId
            )
            
            val accessToken =
                TokenManager.createAccessToken(Json.encodeToString(userJwt))
            val refreshToken =
                TokenManager.createRefreshToken(Json.encodeToString(userJwt))
            
            val userModel = UserModel(
                id = userId,
                email = email,
                username = username,
                profilePicture = "",
                password = hashPassword,
                createdAt = createdAt!!,
                accessToken = accessToken,
                refreshToken = refreshToken,
                fullName = username,
            )
            
            supabase.from("users").insert(userModel)
            
            return true
            
        } catch (e: Exception) {
            Log.e(TAG, "Error registering: ${e.message}")
            throw e
        }
    }
    
    override suspend fun login(email: String, password: String): Boolean {
        try {
            
            val checkUserExist = checkUserExist(email)
            
            if (!checkUserExist) throw Exception("User does not exist, please register first...")
            
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            
            return true
            
        } catch (e: AuthRestException) {
            Log.e(TAG, "AuthRestException login: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error login: ${e.message}")
            throw e
        }
    }
    
    override suspend fun loginWithGoogle(context: Context): Result<Boolean> {
        val credentialManager = CredentialManager.create(context)
        
        val rawNonce = UUID.randomUUID()
            .toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce =
            digest.fold("") { str, it -> str + "%02x".format(it) }
        
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(SUPABASE_SERVER_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()
        
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        
        try {
            
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)
            
            val googleIdToken = googleIdTokenCredential.idToken
            
            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
                nonce = rawNonce
            }
            
            val session = supabase.auth.currentSessionOrNull()
                ?: throw AuthException("No active session found. Please log in again.")
            
            val user = session.user
                ?: throw AuthException("Unable to retrieve user information from the session.")
            
            val userId = user.identities?.get(0)?.userId
                ?: throw AuthException("User ID not found in user identities.")
            
            val userModel = UserModel(
                id = userId,
                email = user.email!!,
                username = user.userMetadata?.get("name")?.toString()
                    ?.trim('\"') ?: "",
                profilePicture = user.userMetadata?.get("avatar_url")
                    .toString().trim('\"') ?: "",
                password = "",
                createdAt = user.createdAt!!,
                accessToken = session.accessToken,
                refreshToken = session.accessToken,
                fullName = user.userMetadata?.get("full_name").toString()
                    .trim('\"'),
            )
            
            val checkUserExist = checkUserExist(userModel.email)
            
            if (!checkUserExist) {
                supabase.from("users").insert(userModel)
            }
            
            return Result.success(true)
            
        } catch (e: AuthException) {
            Log.e(TAG, "Error Auth Exception: ${e.message}")
            return Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e(TAG, "Error parsing Google ID token: ${e.message}")
            return Result.failure(e)
        } catch (e: RestException) {
            Log.e(TAG, "Error Rest Exception: ${e.message}")
            return Result.failure(e)
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (e is GetCredentialException) {
                    Log.e(TAG, "Error Get Credential: ${e.message}")
                    return Result.failure(e)
                }
            } else {
                Log.e(TAG, "Error occurred: ${e.message}")
                return Result.failure(e)
            }
            return Result.failure(e)
        }
    }
    
    override suspend fun checkUserExist(email: String): Boolean {
        try {
            val fetchUserFromDb = supabase.from("users")
                .select(
                    columns = Columns.list("email", "username", "id")
                ) {
                    filter {
                        eq("email", email)
                    }
                }
            
            val user = fetchUserFromDb.data
            
            val parseStringToList = Json.decodeFromString<List<User>>(user)
            
            return parseStringToList.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user: ${e.message}")
            throw e
        }
    }
}