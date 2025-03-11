import com.example.socialmedia.data.model.ResponseModel
import com.example.socialmedia.data.model.UserModel

interface UserDatasource {
    suspend fun search(query: String): ResponseModel<List<UserModel>>
}