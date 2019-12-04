package com.springframework.converters;

import com.springframework.commands.NotesCommand;
import com.springframework.model.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

    public static final Long ID_VALUE =1l;
    public static final String RECIPE_NOTES = "Notes";

    NotesToNotesCommand converter;

    @BeforeEach
    void setUp() {
        converter=new NotesToNotesCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

//    @Test
//    public void testEmptyObject() throws Exception {
//        Notes notes=new Notes();
//        assertNotNull(converter.convert(notes));
//    }
    @Test
    void convert() {
        //given
        Notes notes = new Notes();
        notes.setId(ID_VALUE);
        notes.setRecipeNotes(RECIPE_NOTES);

        //when
        NotesCommand notesCommand = converter.convert(notes);

        //then
        assertNotNull(notesCommand);
        assertEquals(ID_VALUE, notesCommand.getId());
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }
}