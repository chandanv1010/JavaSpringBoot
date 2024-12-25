package chandanv.local.chandanv.modules.users.requests.Permission;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateRequest {
    
    @NotBlank(message = "Tên nhóm thành viên không được để trống")
    private String name;


    @NotNull(message = "Trạng thái không được để trống")
    @Min(value = 0, message="Giá trị trạng thái phải lớn hơn hoặc bằng 0")
    @Max(value = 2, message= "Giá trị trạng thái phải nhỏ hơn hoặc bằng 2")
    private Integer publish;


}
