package com.springframework.services;

import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.springframework.model.UnitOfMeasure;
import com.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnitOfMeasureServiceImpTest {

    UnitOfMeasureService unitOfMeasureService;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand=new UnitOfMeasureToUnitOfMeasureCommand();
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService=new UnitOfMeasureServiceImp(unitOfMeasureRepository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void testListAllUoms() {
        //given
        Set<UnitOfMeasure> unitOfMeasures=new HashSet<>();
        UnitOfMeasure unitOfMeasure1=new UnitOfMeasure();
        unitOfMeasure1.setId(1L);
        UnitOfMeasure unitOfMeasure2=new UnitOfMeasure();
        unitOfMeasure2.setId(2l);

        unitOfMeasures.add(unitOfMeasure1);
        unitOfMeasures.add(unitOfMeasure2);

        //when
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        Set<UnitOfMeasureCommand> unitOfMeasureCommands=unitOfMeasureService.listAllUoms();
        assertEquals(2,unitOfMeasureCommands.size());
        verify(unitOfMeasureRepository).findAll();
    }
}