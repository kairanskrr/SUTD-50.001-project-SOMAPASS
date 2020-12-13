package com.kairan.uidesign;

import android.net.Uri;

import com.kairan.uidesign.Utils.HttpRequest;
import com.kairan.uidesign.Utils.StringsUsed;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /*@Test
    public void getURL_createAccount() throws MalformedURLException {
        String[] createAccount = new String[4];
        createAccount[0] = StringsUsed.CreateAccount_http;
        createAccount[1] = "12";
        createAccount[2] = "12";
        createAccount[3] = "kr";
        URL url = getURL(createAccount);
        assertEquals("https://somapass.xyz/createaccount/12/12/kr",url.toString());
    }

    @Test
    public void getURL_checkIn() throws MalformedURLException {
        String[] checkIn = new String[5];
        checkIn[0] = StringsUsed.CheckIn_http;
        checkIn[1] = "12";
        checkIn[2] = "12";
        checkIn[3] = "1";
        checkIn[4] = "location";
        URL url = getURL(checkIn);
        assertEquals("https://somapass.xyz/checkin/12/12/1/location",url.toString());
    }

    @Test
    public void getURL_Login() throws MalformedURLException {
        String[] login = new String[3];
        login[0] = StringsUsed.Login_http;
        login[1] = "12";
        login[2] = "12";
        URL url = getURL(login);
        assertEquals("https://somapass.xyz/login/12/12",url.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void getURL_exception() throws MalformedURLException {
        String[] exception = new String[3];
        exception[0] = StringsUsed.Login_http;
        exception[1] = "^^";
        exception[2] = "@`3";
        getURL(exception);
    }

    public URL getURL(String[] strings) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("somapass.xyz");
        for(String i:strings){
            builder.appendPath(i);
        }
        Uri uri = builder.build();
        return new URL(uri.toString());
    }*/
    
}