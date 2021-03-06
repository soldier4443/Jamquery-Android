package com.turastory.jamquery.data.datasource;

import com.turastory.jamquery.presentation.vo.Jamquery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by tura on 2018-04-13.
 * <p>
 * Test Retrofit REST API using retrofit-mock + Mockito
 */
public class JamqueryDataRepositoryTest {
    
    @Mock
    private JamqueryDataSource mockLocalDataSource;
    
    @Mock
    private JamqueryDataSource mockRemoteDataSource;
    
    private JamqueryDataRepository repository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new JamqueryDataRepository(mockLocalDataSource, mockRemoteDataSource);
    }
    
    @Test
    public void test_getJamqueryListHappy() {
        JamqueryDataSource.DataSourceCallback mockCallback = mock(JamqueryDataSource.DataSourceCallback.class);
        List<Jamquery> voList = new ArrayList<>();
    
        doAnswer(invocation -> {
            ((JamqueryDataSource.DataSourceCallback) invocation.getArgument(1))
                .onLoad(voList);
            return null;
        }).when(mockLocalDataSource).getJamqueryList(any(String.class),
            any(JamqueryDataSource.DataSourceCallback.class));
    
        repository.getJamqueryList("test", mockCallback);
    
        verify(mockCallback).onLoad(voList);
        verifyNoMoreInteractions(mockCallback);
    }
    
    @Test
    public void test_getJamqueryListError() {
        JamqueryDataSource.DataSourceCallback mockCallback = mock(JamqueryDataSource.DataSourceCallback.class);
        Exception mockException = mock(Exception.class);
    
        doAnswer(invocation -> {
            ((JamqueryDataSource.DataSourceCallback) invocation.getArgument(1))
                .onError(mockException);
            return null;
        }).when(mockLocalDataSource).getJamqueryList(any(String.class),
            any(JamqueryDataSource.DataSourceCallback.class));
    
        repository.getJamqueryList("test", mockCallback);
    
        verify(mockCallback).onError(mockException);
        verifyNoMoreInteractions(mockCallback);
    }
}