package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.entity.ShoppingListEntity;
import shoppinglist.backend.repository.ShoppingListRepository;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository){
        this.shoppingListRepository = shoppingListRepository;
    }


    public List<ShoppingListDto> getAll(){
        return shoppingListRepository.findAll().stream().map(ShoppingListEntity::mapToDto).toList();
    }
}
