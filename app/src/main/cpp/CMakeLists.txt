cmake_minimum_required(VERSION 3.4.1)
#导入bzip
file(GLOB bzib_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

add_library(
        native-lib
        SHARED
        native-lib.cpp
        bspatch.c
        #引用bzip
        ${bzib_source})

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        ${log-lib})