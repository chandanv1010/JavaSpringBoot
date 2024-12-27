package chandanv.local.chandanv.controllers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.services.interfaces.BaseServiceInterface;



/*
 * E - Entity
 * R - Resource
 * C - CreateRequest
 * U - UpdateRequest
 * Repo - Repository
 * M - Mapper
 * S - Service
 */
public abstract class BaseControllerTest<
    E, 
    R,
    C,
    U,
    Repo extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>,
    M extends BaseMapper<E, R, C, U>,
    S extends BaseServiceInterface<E, C, U>
> {


    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected S service;

    @MockBean
    protected M mapper;

   
    protected abstract String getApiPath();
    protected abstract String getTestKeyword();
    protected abstract Map<String, String[]> getTestSimpleFilter();
    protected abstract ResultActions getExpectResponseData(ResultActions result, List<R> resource) throws Exception;
    protected abstract ResultActions getExpectResponseFilterData(ResultActions result, List<R> resource) throws Exception;
    protected abstract List<E> createTestEntities();
    protected abstract List<E> createTestEntitiesByKeywordFiltered(List<E> entities, String keyword);
    protected abstract List<E> createTestEntitiesBySimpleFiltered(List<E> entities, Map<String, String[]> filters);
    protected abstract List<R> createTestResources();
    protected abstract List<R> createTestResourcesByKeywordFiltered(List<R> resources, String keyword);
    protected abstract List<R> createTestResourcesBySimpleFiltered(List<R> resources, Map<String, String[]> filters);

    /* Test trường hợp lấy dữ liệu không có filter --> lấy toàn bộ */
    @Test
    void list_NoFilter_ShouldReturnAllRecords() throws Exception {
        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockEntities);
        when(mapper.toList(mockEntities)).thenReturn(mockResources);

        mockMvc.perform(get(getApiPath() + "/list")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("SUCCESS"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist());
        
        verify(service).getAll(captor.getValue());
        verify(mapper).toList(mockEntities);

    }


    /* Test trường hợp lấy dữ liệu có filter --> keyword */
    @Test
    void list_withKeywordFilter_ShouldReturnFilteredKeywordRecords() throws Exception{

        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();
        List<E> mockFilterEntities = createTestEntitiesByKeywordFiltered(mockEntities, getTestKeyword());
        List<R> mockFilterResources = createTestResourcesByKeywordFiltered(mockResources, getTestKeyword());

        
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockFilterEntities);
        when(mapper.toList(mockFilterEntities)).thenReturn(mockFilterResources);

        ResultActions actions =  mockMvc.perform(get(getApiPath() + "/list")
            .param("keyword", getTestKeyword())
            .contentType(MediaType.APPLICATION_JSON));
            // .andDo(print());
      
        getExpectResponseData(actions, mockFilterResources)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("SUCCESS"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist());

            verify(service).getAll(captor.getValue());
            verify(mapper).toList(mockFilterEntities);
        
            Map<String, String[]> capturedParams = captor.getValue();
            assertThat(capturedParams.get("keyword")).containsExactly(getTestKeyword());

    }

    /* Test trường hợp lấy dữ liệu với filter --> filterSimple dạng where = */
    @Test
    void list_withSimpleFilter_ShouldReturnSimpleFilteredRecords() throws  Exception{
        Map<String, String[]> filters = getTestSimpleFilter();
        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();


        List<E> mockFilterEntities = createTestEntitiesBySimpleFiltered(mockEntities, filters);
        List<R> mockFilterResources = createTestResourcesBySimpleFiltered(mockResources, filters);
       

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockFilterEntities);
        when(mapper.toList(mockFilterEntities)).thenReturn(mockFilterResources);

        MockHttpServletRequestBuilder requestBuilder = get(getApiPath() + "/list");
        for(Map.Entry<String, String[]> entry: filters.entrySet()){
            String key = entry.getKey();
            String[] values = entry.getValue();
            for(String value: values){
                requestBuilder.param(key, value);
            }
        }

        ResultActions actions =  mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        getExpectResponseFilterData(actions, mockFilterResources)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("SUCCESS"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist());
        
       
        Map<String, String[]> capturedParams = captor.getValue();
        for(Map.Entry<String, String[]> entry: filters.entrySet()){
            String key = entry.getKey();
            String[] values = entry.getValue();
            assertThat(capturedParams).containsKey(key);
            assertThat(capturedParams.get(key)).containsExactly(values);
        }
        verify(service).getAll(captor.getValue());
        verify(mapper).toList(mockFilterEntities);
    }


    /* Test trường hợp lấy dữ liệu với filter --> filterComplex dạng ví dụ id[gt], price[gte] ... */


    /* Test trường hợp kết hợp tất cả các loại filter với nhau */



    /* Test trường hợp bị lỗi Error */

    
}
