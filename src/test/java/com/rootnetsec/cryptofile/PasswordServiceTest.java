package com.rootnetsec.cryptofile;

import org.junit.Assert;
import org.junit.Test;

public class PasswordServiceTest {

    @Test
    public void searchHaveIBeenPwnedDatabase() {
        Assert.assertTrue(PasswordService.searchHaveIBeenPwnedDatabase("zaq12wsx"));
        Assert.assertTrue(PasswordService.searchHaveIBeenPwnedDatabase("qwerty"));
        Assert.assertFalse(PasswordService.searchHaveIBeenPwnedDatabase("B@RDZ0_TRuDn3_h@$lo"));
        Assert.assertFalse(PasswordService.searchHaveIBeenPwnedDatabase("JIShaisbi9123uija0q"));
        Assert.assertFalse(PasswordService.searchHaveIBeenPwnedDatabase("UUU___Aasl321KKKKKKKaaa"));
    }
}