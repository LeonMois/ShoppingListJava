package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shoppinglist.backend.dto.UnitDto;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.repository.UnitRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("UnitService Tests")
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitService unitService;

    private UnitEntity testUnit;
    private UnitDto testUnitDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUnit = new UnitEntity();
        testUnit.setId(1);
        testUnit.setUnitName("kg");

        testUnitDto = new UnitDto("kg");
    }

    @Test
    @DisplayName("Should add a new unit successfully")
    void testAddUnit_Success() throws IOException {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(null);
        when(unitRepository.save(any(UnitEntity.class))).thenReturn(testUnit);

        UnitDto result = unitService.addUnit("kg");

        assertNotNull(result);
        assertEquals("kg", result.getUnitName());
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, times(1)).save(any(UnitEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate unit")
    void testAddUnit_Duplicate() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(testUnit);

        assertThrows(IOException.class, () -> unitService.addUnit("kg"));
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, never()).save(any(UnitEntity.class));
    }

    @Test
    @DisplayName("Should add multiple units with some duplicates")
    void testAddUnitList() throws IOException {
        List<UnitDto> unitList = Arrays.asList(
            new UnitDto("kg"),
            new UnitDto("ml"),
            new UnitDto("kg")
        );

        UnitEntity unit1 = new UnitEntity();
        unit1.setUnitName("kg");
        UnitEntity unit2 = new UnitEntity();
        unit2.setUnitName("ml");

        when(unitRepository.findEntityByUnitNameIgnoreCase("kg"))
            .thenReturn(null)   // first "kg" — not found, will be saved
            .thenReturn(unit1); // second "kg" — already exists, throws
        when(unitRepository.findEntityByUnitNameIgnoreCase("ml"))
            .thenReturn(null);  // "ml" — not found, will be saved
        when(unitRepository.save(any(UnitEntity.class)))
            .thenReturn(unit1)
            .thenReturn(unit2);
        when(unitRepository.findAll()).thenReturn(Arrays.asList(unit1, unit2));

        List<UnitDto> result = unitService.addUnitList(unitList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(unitRepository, times(2)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("ml");
        verify(unitRepository, times(2)).save(any(UnitEntity.class));
        verify(unitRepository).findAll();
    }

    @Test
    @DisplayName("Should delete a unit successfully")
    void testDeleteUnit_Success() throws IOException {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(testUnit);

        UnitDto result = unitService.deleteUnit("kg");

        assertNotNull(result);
        assertEquals("kg", result.getUnitName());
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, times(1)).delete(testUnit);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent unit")
    void testDeleteUnit_NotFound() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(null);

        assertThrows(IOException.class, () -> unitService.deleteUnit("kg"));
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update unit name successfully")
    void testUpdateUnit_Success() throws IOException {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(testUnit);
        when(unitRepository.save(testUnit)).thenReturn(testUnit);

        UnitDto result = unitService.updateUnit("kg", "lb");

        assertNotNull(result);
        assertEquals("lb", result.getUnitName());
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, times(1)).save(testUnit);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent unit")
    void testUpdateUnit_NotFound() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(null);

        assertThrows(IOException.class, () -> unitService.updateUnit("kg", "lb"));
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get all units")
    void testGetAllUnits() {
        UnitEntity unit2 = new UnitEntity();
        unit2.setUnitName("ml");

        when(unitRepository.findAll()).thenReturn(Arrays.asList(testUnit, unit2));

        List<UnitDto> result = unitService.getAllUnits();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("kg", result.get(0).getUnitName());
        assertEquals("ml", result.get(1).getUnitName());
        verify(unitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get or add unit - existing unit")
    void testGetOrAddUnit_Existing() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(testUnit);

        UnitEntity result = unitService.getOrAddUnit("kg");

        assertNotNull(result);
        assertEquals("kg", result.getUnitName());
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get or add unit - new unit")
    void testGetOrAddUnit_New() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg"))
            .thenReturn(null)
            .thenReturn(testUnit);
        when(unitRepository.save(any(UnitEntity.class))).thenReturn(testUnit);

        UnitEntity result = unitService.getOrAddUnit("kg");

        assertNotNull(result);
        assertEquals("kg", result.getUnitName());
        verify(unitRepository, times(2)).findEntityByUnitNameIgnoreCase("kg");
        verify(unitRepository, times(1)).save(any(UnitEntity.class));
    }

    @Test
    @DisplayName("Should get unit by name")
    void testGetUnit_Success() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(testUnit);

        UnitEntity result = unitService.getUnit("kg");

        assertNotNull(result);
        assertEquals("kg", result.getUnitName());
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent unit")
    void testGetUnit_NotFound() {
        when(unitRepository.findEntityByUnitNameIgnoreCase("kg")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> unitService.getUnit("kg"));
        verify(unitRepository, times(1)).findEntityByUnitNameIgnoreCase("kg");
    }
}
