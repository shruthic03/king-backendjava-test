package com.king.java.backend.test.javatest.configurations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.reflect.Whitebox;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(LoginService.class)
public class LoginServiceTest {

    private LoginService service;

    public LoginServiceTest()
    {

    }

    @Before
    public void setUp()
    {
        service = new LoginService(11, 11, 26, 10);
    }

    @Test
    public void doLogin_CheckSessionID()
    {
        String sessionId = service.doLogin("abc");
        Map sessionInfoMap = Whitebox.getInternalState(service, "sessionInfoMap");
        Assert.assertEquals(26, sessionId.length());
        Assert.assertEquals(1, sessionInfoMap.size());
    }
}