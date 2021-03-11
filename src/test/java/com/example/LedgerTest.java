package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import static org.junit.runners.Parameterized.*;

@RunWith(Suite.class)
@SuiteClasses({
        Ledger.class,
        Verifier.class })

public class LedgerTest {
    
    @Test
    public void throwsIllegalArgumentExceptionIfIconIsNull() {
      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("Negative values not allowed.");
      Ledger t = new Ledger();
      t.methodToBeTest(-12345);
    }

}