package com.turastory.jamquery.domain.usecase;

import com.turastory.jamquery.data.rqrs.GetJamqueryListRq;
import com.turastory.jamquery.data.rqrs.GetJamqueryListRs;
import com.turastory.jamquery.domain.ThreadExecutor;
import com.turastory.jamquery.domain.UIThreadExecutor;
import com.turastory.jamquery.domain.repository.JamqueryRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by tura on 2018-04-12.
 */
public class GetJamqueryListUseCaseTest {
    
    private GetJamqueryListUseCase useCase;
    
    @Mock
    private JamqueryRepository mockJamqueryRepository;
    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private UIThreadExecutor mockUiThreadExecutor;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        useCase = new GetJamqueryListUseCaseImpl(mockJamqueryRepository, mockThreadExecutor,
            mockUiThreadExecutor);
    }
    
    @Test
    public void test_execution() {
        String keyWord = "asdf";
        
        useCase.execute(keyWord, mock(GetJamqueryListUseCase.UseCaseCallback.class));
        
        verify(mockThreadExecutor).execute(any(Runnable.class));
        verifyNoMoreInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockJamqueryRepository);
        verifyZeroInteractions(mockUiThreadExecutor);
    }
    
    @Test
    public void test_useCaseRun() {
        String keyWord = "asdf";
        
        useCase.execute(keyWord, mock(GetJamqueryListUseCase.UseCaseCallback.class));
        useCase.run();
        
        verify(mockThreadExecutor).execute(any(Runnable.class));
        verifyNoMoreInteractions(mockThreadExecutor);
        verify(mockJamqueryRepository).getJamqueryList(any(GetJamqueryListRq.class),
            any(JamqueryRepository.RepositoryCallback.class));
        verifyNoMoreInteractions(mockJamqueryRepository);
        verifyZeroInteractions(mockUiThreadExecutor);
    }
    
    @Test
    public void test_repositoryCallbackSuccess() {
        String keyWord = "asdf";
        
        List<GetJamqueryListRs> mockRsList = mock(List.class);
        
        doAnswer(invocation -> {
            ((JamqueryRepository.RepositoryCallback) invocation.getArgument(1))
                .onLoad(mockRsList);
            return null;
        }).when(mockJamqueryRepository).getJamqueryList(any(GetJamqueryListRq.class),
            any(JamqueryRepository.RepositoryCallback.class));
        
        GetJamqueryListUseCase.UseCaseCallback mockCallback =
            mock(GetJamqueryListUseCase.UseCaseCallback.class);
        
        useCase.execute(keyWord, mockCallback);
        useCase.run();
        
        verify(mockUiThreadExecutor).post(any(Runnable.class));
        verifyNoMoreInteractions(mockUiThreadExecutor);
        verifyZeroInteractions(mockRsList);
    }
    
    @Test
    public void test_repositoryCallbackError() {
        String keyWord = "asdf";
        
        Exception mockException = mock(Exception.class);
        
        doAnswer(invocation -> {
            ((JamqueryRepository.RepositoryCallback) invocation.getArgument(1))
                .onError(mockException);
            return null;
        }).when(mockJamqueryRepository).getJamqueryList(any(GetJamqueryListRq.class),
            any(JamqueryRepository.RepositoryCallback.class));
        
        GetJamqueryListUseCase.UseCaseCallback mockCallback =
            mock(GetJamqueryListUseCase.UseCaseCallback.class);
        
        useCase.execute(keyWord, mockCallback);
        useCase.run();
        
        verify(mockUiThreadExecutor).post(any(Runnable.class));
        verifyNoMoreInteractions(mockUiThreadExecutor);
        verifyZeroInteractions(mockException);
    }
}