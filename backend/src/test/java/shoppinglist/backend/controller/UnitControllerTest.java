package shoppinglist.backend.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.UnitDto;
import shoppinglist.backend.service.UnitService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnitController.class)
@DisplayName("UnitController Tests")
class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UnitService unitService;

    @Test
    @DisplayName("GET /units should return all units")
    void testGetUnits() throws Exception {
        when(unitService.getAllUnits()).thenReturn(List.of(new UnitDto("kg")));

        mockMvc.perform(get("/units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].unitName").value("kg"));

        verify(unitService, times(1)).getAllUnits();
    }

    @Test
    @DisplayName("POST /units/add should add a unit")
    void testAddUnit() throws Exception {
        UnitDto unit = new UnitDto("kg");
        when(unitService.addUnit("kg")).thenReturn(unit);

        mockMvc.perform(post("/units/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitName").value("kg"));

        verify(unitService, times(1)).addUnit("kg");
    }

    @Test
    @DisplayName("POST /units/addList should add a list of units")
    void testAddUnitList() throws Exception {
        UnitDto unit1 = new UnitDto("kg");
        UnitDto unit2 = new UnitDto("liter");
        when(unitService.addUnitList(anyList())).thenReturn(Arrays.asList(unit1, unit2));

        mockMvc.perform(post("/units/addList")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(unit1, unit2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(unitService, times(1)).addUnitList(anyList());
    }

    @Test
    @DisplayName("DELETE /units/delete should delete a unit")
    void testDeleteUnit() throws Exception {
        UnitDto unit = new UnitDto("kg");
        when(unitService.deleteUnit("kg")).thenReturn(unit);

        mockMvc.perform(delete("/units/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitName").value("kg"));

        verify(unitService, times(1)).deleteUnit("kg");
    }

    @Test
    @DisplayName("PUT /units/update should update a unit")
    void testUpdateUnit() throws Exception {
        UnitDto oldUnit = new UnitDto("kg");
        UnitDto newUnit = new UnitDto("gram");
        when(unitService.updateUnit("kg", "gram")).thenReturn(newUnit);

        mockMvc.perform(put("/units/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(oldUnit, newUnit))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitName").value("gram"));

        verify(unitService, times(1)).updateUnit("kg", "gram");
    }

    @Test
    @DisplayName("POST /units/add should propagate IOException when unit already exists")
    void testAddUnit_ThrowsIOException() throws Exception {
        UnitDto unit = new UnitDto("kg");
        when(unitService.addUnit("kg")).thenThrow(new IOException("Unit already exists"));

        assertThrows(IOException.class, () -> mockMvc.perform(post("/units/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(unit))));
    }
}
