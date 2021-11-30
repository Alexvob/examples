package com.astudio.progressmonitor.task;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TabHostPresenterTest {

    private TabHostPresenter mPresenter;

    @Before
    public void setUp() {
        mPresenter = new TabHostPresenter();
    }

    @Test
    public void test2(){
        assertEquals(5, mPresenter.testTesting(5));
    }

}