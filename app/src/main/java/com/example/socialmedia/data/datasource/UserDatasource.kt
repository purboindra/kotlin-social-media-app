import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel

interface UserDatasource {
    suspend fun fetchAllUsers(query: String?): ResponseModel<List<UserModel>>
    suspend fun fetchUserById(userId: String): ResponseModel<UserModel>
}