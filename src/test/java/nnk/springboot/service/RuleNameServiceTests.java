package nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.RuleName;
import nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class RuleNameServiceTests {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Mock
    private RuleName ruleName1;

    @Mock
    private RuleName ruleName2;

    @InjectMocks
    private RuleNameService ruleNameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler le comportement des objets RuleName
        when(ruleName1.getId()).thenReturn(1);
        when(ruleName1.getName()).thenReturn("Rule1");
        when(ruleName1.getDescription()).thenReturn("Description1");
        when(ruleName1.getJson()).thenReturn("{\"key\":\"value\"}");
        when(ruleName1.getTemplate()).thenReturn("Template1");
        when(ruleName1.getSqlStr()).thenReturn("SELECT * FROM table WHERE condition");
        when(ruleName1.getSqlPart()).thenReturn("AND condition = 'value'");

        when(ruleName2.getId()).thenReturn(2);
        when(ruleName2.getName()).thenReturn("Rule2");
        when(ruleName2.getDescription()).thenReturn("Description2");
        when(ruleName2.getJson()).thenReturn("{\"key\":\"another_value\"}");
        when(ruleName2.getTemplate()).thenReturn("Template2");
        when(ruleName2.getSqlStr()).thenReturn("SELECT * FROM table WHERE other_condition");
        when(ruleName2.getSqlPart()).thenReturn("AND other_condition = 'another_value'");
    }

    @Test
    public void testGetAllRuleName() {
        // Arrange
        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(ruleName1, ruleName2));

        // Act
        List<RuleName> result = ruleNameService.getAllRuleName();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    public void testAddRuleName() {
        // Arrange
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName1);

        // Act
        ruleNameService.addRuleName(ruleName1);

        // Assert
        verify(ruleNameRepository, times(1)).save(ruleName1);
    }

    @Test
    public void testGetByIdRuleName() {
        // Arrange
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));

        // Act
        RuleName result = ruleNameService.getByIdRuleName(1);

        // Assert
        assertNotNull(result);
        assertEquals(ruleName1, result);
        verify(ruleNameRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateRuleName() {
        // Arrange
        RuleName updatedRuleName = mock(RuleName.class);
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName1));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName1);

        // Simuler que le `updatedRuleName` possède des données modifiées
        when(updatedRuleName.getName()).thenReturn("UpdatedRule1");
        when(updatedRuleName.getDescription()).thenReturn("UpdatedDescription1");
        when(updatedRuleName.getJson()).thenReturn("{\"newKey\":\"newValue\"}");
        when(updatedRuleName.getTemplate()).thenReturn("UpdatedTemplate1");
        when(updatedRuleName.getSqlStr()).thenReturn("SELECT * FROM table WHERE updated_condition");
        when(updatedRuleName.getSqlPart()).thenReturn("AND updated_condition = 'new_value'");

        // Act
        ruleNameService.updateRuleName(1, updatedRuleName);

        // Assert
        verify(ruleNameRepository, times(1)).findById(1);
        verify(ruleNameRepository, times(1)).save(ruleName1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(ruleNameRepository.existsById(1)).thenReturn(true);
        doNothing().when(ruleNameRepository).deleteById(1);

        // Act
        ruleNameService.deleteById(1);

        // Assert
        verify(ruleNameRepository, times(1)).deleteById(1);
    }
}

