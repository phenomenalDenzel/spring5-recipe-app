package com.springframework.converters;

import com.springframework.commands.CategoryCommand;
import com.springframework.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {
    public final long ID_VALUE=1l;
    public final String CATEGORY_NAME="description";

    CategoryCommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter=new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new CategoryCommand()));
    }
    @Test
    void convert() {
        //given
        CategoryCommand categoryCommand=new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setCategoryName(CATEGORY_NAME);

        //when
        Category category=converter.convert(categoryCommand);

        //then
        assertEquals(CATEGORY_NAME,category.getCategoryName());
        assertEquals(ID_VALUE,category.getId());
    }
}