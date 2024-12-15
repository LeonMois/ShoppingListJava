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

    @PostMapping(path = "/{unit}")
    public UnitDto addUnit(@PathVariable String unit) throws IOException {

        return unitService.addUnit(unit);
    }

    @DeleteMapping(path = "/{unit}")
    public UnitDto deleteUnit(@PathVariable String unit) throws IOException {

        return unitService.deleteUnit(unit);
    }

    @PutMapping(path = "/{oldUnit}/{newUnit}")
    public UnitDto updateUnit(@PathVariable("oldUnit") String oldUnit, @PathVariable("newUnit") String newUnit) throws IOException {

        return unitService.updateUnit(oldUnit, newUnit);
    }
}
