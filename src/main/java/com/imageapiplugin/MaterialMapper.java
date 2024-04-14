package com.imageapiplugin;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialMapper {
    private Map<String, Material> materialMap = new HashMap<>();

    public MaterialMapper() {
        for (Material mat : Material.values()) {
            String normalizedMaterialName = normalizeName(mat.name());
            materialMap.put(normalizedMaterialName, mat);
        }
    }

    private String normalizeName(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    public Material getMaterialFromImageName(String imageName) {
        String baseName = imageName.substring(0, imageName.lastIndexOf('.'));
        String normalizedImageName = normalizeName(baseName);

        return materialMap.getOrDefault(normalizedImageName, Material.AIR); // Default
    }
}
