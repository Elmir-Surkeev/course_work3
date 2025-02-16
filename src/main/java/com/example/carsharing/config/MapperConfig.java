package com.example.carsharing.config;

import org.mapstruct.NullValueCheckStrategy;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {
}
