package chandanv.local.chandanv.modules.users.requests.User;

import java.util.List;

import chandanv.local.chandanv.annotations.UniqueEmail;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class StoreRequest {
    
    @NotBlank(message = "Tên thành viên không được để trống")
    private String name;

    @UniqueEmail
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    private String address;
    private String image;


    @NotNull(message = "Trạng thái không được để trống")
    @Min(value = 0, message="Giá trị trạng thái phải lớn hơn hoặc bằng 0")
    @Max(value = 2, message= "Giá trị trạng thái phải nhỏ hơn hoặc bằng 2")
    private Integer publish;


    @NotNull(message = "Chưa cấp quyền cho thành viên")
    private List<Long> userCatalogues;
   
}
