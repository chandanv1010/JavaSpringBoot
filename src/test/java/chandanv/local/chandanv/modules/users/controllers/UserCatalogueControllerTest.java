package chandanv.local.chandanv.modules.users.controllers;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import chandanv.local.chandanv.config.SecurityConfig;
import chandanv.local.chandanv.controllers.BaseControllerTest;
import chandanv.local.chandanv.helpers.JwtAuthFilter;
import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.mappers.UserCatalogueMapper;
import chandanv.local.chandanv.modules.users.repositories.UserCatalogueRepository;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserCatalogueResource;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;



@WebMvcTest(value = UserCatalogueController.class, excludeFilters= @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes={JwtAuthFilter.class, SecurityConfig.class}))
@AutoConfigureMockMvc(addFilters=false)
@AutoConfigureDataJpa
public class UserCatalogueControllerTest extends BaseControllerTest<
    UserCatalogue,
    UserCatalogueResource,
    StoreRequest,
    UpdateRequest,
    UserCatalogueRepository,
    UserCatalogueMapper,
    UserCatalogueServiceInterface
> {
    
    @Override
    protected String getApiPath(){
        return "/api/v1/user_catalogues";
    }

    @Override
    protected String getTestKeyword(){
        return "Admin";
    }

    @Override
    protected List<UserCatalogue> createTestEntities(){
        return Arrays.asList(
            UserCatalogue.builder()
                .id(1L)
                .name("Nhóm Admin")
                .publish(1)
                .createdAt(LocalDateTime.now())
                .build(),
            UserCatalogue.builder()
                .id(2L)
                .name("Nhóm Seller")
                .publish(1)
                .createdAt(LocalDateTime.now())
                .build(),
            UserCatalogue.builder()
                .id(3L)
                .name("Nhóm Cộng Tác Viên")
                .publish(2)
                .createdAt(LocalDateTime.now())
                .build(),
            UserCatalogue.builder()
                .id(4L)
                .name("Nhóm Admin hỗ trợ")
                .publish(2)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }
    

    @Override
    protected List<UserCatalogueResource> createTestResources(){
        return Arrays.asList(
            UserCatalogueResource.builder()
                .id(1L)
                .name("Nhóm Admin")
                .publish(1)
                .build(),
            UserCatalogueResource.builder()
                .id(2L)
                .name("Nhóm Seller")
                .publish(1)
                .build(),
            UserCatalogueResource.builder()
                .id(2L)
                .name("Nhóm Cộng Tác Viên")
                .publish(2)
                .build(),
            UserCatalogueResource.builder()
                .id(2L)
                .name("Nhóm Admin Hỗ Trợ")
                .publish(2)
                .build()
        );
    }

    @Override
    protected List<UserCatalogue> createTestEntitiesByKeywordFiltered(List<UserCatalogue> entities, String keyword){
        String lowerCaseKeyword = keyword.toLowerCase();
        return entities.stream()
            .filter(entry -> entry.getName().toLowerCase().contains(lowerCaseKeyword))
            .collect(Collectors.toList());
    }

    @Override
    protected List<UserCatalogueResource> createTestResourcesByKeywordFiltered(List<UserCatalogueResource> resources, String keyword){
        String lowerCaseKeyword = keyword.toLowerCase();
        return resources.stream()
            .filter(entry -> entry.getName().toLowerCase().contains(lowerCaseKeyword))
            .collect(Collectors.toList());
    }

    @Override
    protected List<UserCatalogue> createTestEntitiesBySimpleFiltered(List<UserCatalogue> entities, Map<String, String[]> filters){
        return entities.stream()
            .filter(entry -> filters.entrySet().stream()
                .allMatch(param -> {
                    try {
                        String key = param.getKey(); // publish
                        String[] values = param.getValue();
                        String getterMethod = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                        Method getter = entry.getClass().getMethod(getterMethod);
                        Object fieldValue = getter.invoke(entry);
                        if(fieldValue == null) return true;
                        return Arrays.stream(values).map(value -> fieldValue instanceof Integer ? Integer.valueOf(value) : value)
                            .allMatch(value -> value.equals(fieldValue));
                    } catch (NumberFormatException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        return true;
                    }
                })
            )
            .collect(Collectors.toList());
    }

    @Override
    protected List<UserCatalogueResource> createTestResourcesBySimpleFiltered(List<UserCatalogueResource> entities, Map<String, String[]> filters){
        return entities.stream()
        .filter(entry -> filters.entrySet().stream()
            .allMatch(param -> {
                try {
                    String key = param.getKey(); // publish
                    String[] values = param.getValue();
                    String getterMethod = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                    Method getter = entry.getClass().getMethod(getterMethod);
                    Object fieldValue = getter.invoke(entry);
                    if(fieldValue == null) return true;
                    return Arrays.stream(values).map(value -> fieldValue instanceof Integer ? Integer.valueOf(value) : value)
                        .allMatch(value -> value.equals(fieldValue));
                } catch (NumberFormatException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    return true;
                }
            })
        )
        .collect(Collectors.toList());
    }

    @Override
    protected ResultActions getExpectResponseData(ResultActions result, List<UserCatalogueResource> resource) throws  Exception{
       
        result.andExpect(jsonPath("$.data", hasSize(resource.size())));
        for(int i = 0; i < resource.size(); i++){
            result.andExpect(jsonPath("$.data["+ i +"].id").value(resource.get(i).getId()))
            .andExpect(jsonPath("$.data["+ i +"].name", containsString(getTestKeyword())))
            .andExpect(jsonPath("$.data["+ i +"].publish").value(resource.get(i).getPublish()));
        }

        return result;
    }

    @Override
    protected ResultActions getExpectResponseFilterData(ResultActions result, List<UserCatalogueResource> resource) throws  Exception{
       
        result.andExpect(jsonPath("$.data", hasSize(resource.size())));
        for(int i = 0; i < resource.size(); i++){
            result.andExpect(jsonPath("$.data["+ i +"].id").value(resource.get(i).getId()))
            .andExpect(jsonPath("$.data["+ i +"].name").value(resource.get(i).getName()))
            .andExpect(jsonPath("$.data["+ i +"].publish").value(resource.get(i).getPublish()));
        }

        return result;
    }


    @Override
    protected Map<String, String[]> getTestSimpleFilter(){
        Map<String, String[]> params = new HashMap<>();
        params.put("publish", new String[] {"2"});

        return params;
    }


}
