package com.hutech.tests3.Controllers;

import com.hutech.tests3.Entities.Menu;
import com.hutech.tests3.Services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restapi")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestApiMenu {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getMenus() {
        List<Menu> menus = menuService.getMenus();
        List<Menu> uniqueMenus = removeDuplicates(menus);
        return ResponseEntity.ok(uniqueMenus);
    }
    private List<Menu> removeDuplicates(List<Menu> menus) {
        Map<String, Menu> seen = new HashMap<>();
        return removeDuplicatesHelper(menus, seen);
    }

    private List<Menu> removeDuplicatesHelper(List<Menu> menus, Map<String, Menu> seen) {
        List<Menu> uniqueMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (!seen.containsKey(menu.getId_menu())) {
                seen.put(menu.getId_menu(), menu);
                if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                    menu.setChildren(removeDuplicatesHelper(menu.getChildren(), seen));
                }
                uniqueMenus.add(menu); // Add only if it's unique
            }
        }
        return uniqueMenus;
    }
}
