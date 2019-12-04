package com.springframework.converters;

import com.springframework.commands.CategoryCommand;
import com.springframework.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryToCategoryCommandTest {
    public final long ID_VALUE=1l;
    public final String CATEGORY_NAME="description";

    CategoryToCategoryCommand converter;

    @BeforeEach
    void setUp() {
        converter=new CategoryToCategoryCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

//    @Test
//    public void testEmptyObject() throws Exception {
//        Category cat=new Category();
//        assertNotNull(converter.convert(cat));
//    }
    @Test
    void convert() {
        //given
        Category category = new Category();
        category.setId(ID_VALUE);
        category.setCategoryName(CATEGORY_NAME);

        //when
        CategoryCommand categoryCommand = converter.convert(category);

        //then
        assertEquals(ID_VALUE, categoryCommand.getId());
        assertEquals(CATEGORY_NAME, categoryCommand.getCategoryName());
    }
}