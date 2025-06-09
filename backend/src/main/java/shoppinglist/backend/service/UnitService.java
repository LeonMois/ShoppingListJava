package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.UnitDto;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.repository.UnitRepository;

import java.io.IOException;
import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public UnitDto addUnit(String unit) throws IOException {
        UnitEntity entity = unitRepository.findEntityByUnitNameIgnoreCase(unit);
        if (entity != null) {
            // todo: write proper exception statuses
            throw new IOException("Unit already exists!");
        }
        UnitEntity unitEntity = new UnitEntity();
        unitEntity.setUnitName(unit);
        unitRepository.save(unitEntity);
        return new UnitDto(unit);
    }

    public List<UnitDto> addUnitList(List<UnitDto> units) throws IOException {

        for (UnitDto unitDto : units) {
            try {
                addUnit(unitDto.getUnitName());
            } catch (IOException e
            ) {
                System.out.println(e.getMessage());
            }
        }
        return unitRepository.findAll().stream().map(entity -> new UnitDto(entity.getUnitName())).toList();
    }

    public UnitDto deleteUnit(String unit) throws IOException {
        UnitEntity categoryEntity = unitRepository.findEntityByUnitNameIgnoreCase(unit);
        if (categoryEntity == null) {
            throw new IOException("Unit doesn't exist!");
        }
        unitRepository.delete(categoryEntity);
        return new UnitDto(categoryEntity.getUnitName());
    }

    public UnitDto updateUnit(String oldName, String newName) throws IOException {
        UnitEntity entity = unitRepository.findEntityByUnitNameIgnoreCase(oldName);
        if (entity == null) {
            throw new IOException("Category doesn't exist!");
        }
        entity.setUnitName(newName);
        unitRepository.save(entity);
        return new UnitDto(newName);
    }

    public List<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream().map(unitEntity -> new UnitDto(unitEntity.getUnitName())).toList();
    }

    public UnitEntity getOrAddUnit(String name) {
        UnitEntity existingEntity = unitRepository.findEntityByUnitNameIgnoreCase(name);
        if (existingEntity != null) {
            return existingEntity;
        }
        UnitEntity newEntity = new UnitEntity();
        newEntity.setUnitName(name);
        unitRepository.save(newEntity);
        return unitRepository.findEntityByUnitNameIgnoreCase(name);
    }

    public UnitEntity getUnit(String name) {
        UnitEntity existingEntity = unitRepository.findEntityByUnitNameIgnoreCase(name);
        if (existingEntity == null) {
            throw new IllegalArgumentException("Unit doesnt exist");
        }
        return existingEntity;

    }

}
