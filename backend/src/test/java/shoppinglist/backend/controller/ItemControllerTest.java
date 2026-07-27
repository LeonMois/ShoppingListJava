package shoppinglist.backend.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.service.ItemService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@DisplayName("ItemController Tests")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    @Test
    @DisplayName("GET /items should return all items")
    void testGetItems() throws Exception {
        ItemDto item = new ItemDto("Apple", "Fruits", "kg");
        when(itemService.getAllItems()).thenReturn(List.of(item));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"));

        verify(itemService, times(1)).getAllItems();
    }

    @Test
    @DisplayName("POST /items/item/add should add an item")
    void testInsertItem() throws Exception {
        ItemDto item = new ItemDto("Apple", "Fruits", "kg");
        when(itemService.addItem(any(ItemDto.class))).thenReturn(item);

        mockMvc.perform(post("/items/item/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));

        verify(itemService, times(1)).addItem(any(ItemDto.class));
    }

    @Test
    @DisplayName("DELETE /items/item/delete should delete an item")
    void testDeleteItem() throws Exception {
        ItemDto item = new ItemDto("Apple", "Fruits", "kg");
        when(itemService.deleteItem(any(ItemDto.class))).thenReturn(item);

        mockMvc.perform(delete("/items/item/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));

        verify(itemService, times(1)).deleteItem(any(ItemDto.class));
    }

    @Test
    @DisplayName("PUT /items/item/update should update an item")
    void testUpdateItem() throws Exception {
        ItemDto oldItem = new ItemDto("Apple", "Fruits", "kg");
        ItemDto newItem = new ItemDto("AppleRed", "Fruits", "kg");
        when(itemService.updateItem(any(ItemDto.class), any(ItemDto.class))).thenReturn(newItem);

        mockMvc.perform(put("/items/item/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(oldItem, newItem))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AppleRed"));

        verify(itemService, times(1)).updateItem(any(ItemDto.class), any(ItemDto.class));
    }

    @Test
    @DisplayName("POST /items/item/addList should add a list of items")
    void testInsertItemList() throws Exception {
        ItemDto item1 = new ItemDto("Apple", "Fruits", "kg");
        ItemDto item2 = new ItemDto("Banana", "Fruits", "kg");
        when(itemService.addItemList(anyList())).thenReturn(Arrays.asList(item1, item2));

        mockMvc.perform(post("/items/item/addList")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(item1, item2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(itemService, times(1)).addItemList(anyList());
    }

    @Test
    @DisplayName("POST /items/item/add should propagate IOException when item already exists")
    void testInsertItem_ThrowsIOException() throws Exception {
        ItemDto item = new ItemDto("Apple", "Fruits", "kg");
        when(itemService.addItem(any(ItemDto.class))).thenThrow(new IOException("Item already exists"));

        assertThrows(IOException.class, () -> mockMvc.perform(post("/items/item/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item))));
    }
}
