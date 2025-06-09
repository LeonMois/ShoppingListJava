package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.UnitDto;
import shoppinglist.backend.service.UnitService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public List<UnitDto> getUnits() {

        return unitService.getAllUnits();
    }

    @PostMapping(path = "/add")
    public UnitDto addUnit(@RequestBody UnitDto unit) throws IOException {

        return unitService.addUnit(unit.getUnitName());
    }

    @PostMapping(path = "/addList")
    public List<UnitDto> addUnitList(@RequestBody List<UnitDto> units) throws IOException {

        return unitService.addUnitList(units);
    }


    @DeleteMapping(path = "/delete")
    public UnitDto deleteUnit(@RequestBody UnitDto unit) throws IOException {

        return unitService.deleteUnit(unit.getUnitName());
    }

    @PutMapping(path = "/update")
    public UnitDto updateUnit(@RequestBody List<UnitDto> units) throws IOException {

        return unitService.updateUnit(units.getFirst().getUnitName(), units.getLast().getUnitName());
    }
}
