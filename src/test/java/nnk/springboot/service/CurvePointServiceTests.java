package nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.CurvePoint;
import nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class CurvePointServiceTests {

    @Mock
    private CurvePointRepository curvePointRepository;

    @Mock
    private CurvePoint curvePoint1;

    @Mock
    private CurvePoint curvePoint2;

    @InjectMocks
    private CurvePointService curvePointService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(curvePoint1.getId()).thenReturn(1);
        when(curvePoint1.getTerm()).thenReturn(1.5);
        when(curvePoint1.getValue()).thenReturn(100.0);

        when(curvePoint2.getId()).thenReturn(2);
        when(curvePoint2.getTerm()).thenReturn(2.0);
        when(curvePoint2.getValue()).thenReturn(200.0);
    }

    @Test
    public void testGetAllCurvePoint() {
        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(curvePoint1, curvePoint2));

        List<CurvePoint> result = curvePointService.getAllCurvePoint();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(curvePointRepository, times(1)).findAll();
    }

    @Test
    public void testAddCurvePoint() {
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint1);

        curvePointService.addCurvePoint(curvePoint1);

        verify(curvePointRepository, times(1)).save(curvePoint1);
    }

    @Test
    public void testGetCurvePointById() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));

        CurvePoint result = curvePointService.getCurvePointById(1);

        assertNotNull(result);
        assertEquals(curvePoint1, result);
        verify(curvePointRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateCurvePoint() {
        CurvePoint updatedCurvePoint = mock(CurvePoint.class);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint1));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint1);

        when(updatedCurvePoint.getTerm()).thenReturn(2.5);
        when(updatedCurvePoint.getValue()).thenReturn(300.0);

        curvePointService.updateCurvePoint(1, updatedCurvePoint);
        verify(curvePointRepository, times(1)).findById(1);
        verify(curvePointRepository, times(1)).save(curvePoint1);
    }

    @Test
    public void testDeleteById() {
        when(curvePointRepository.existsById(1)).thenReturn(true);
        doNothing().when(curvePointRepository).deleteById(1);

        curvePointService.deleteById(1);

        verify(curvePointRepository, times(1)).deleteById(1);
    }
}
