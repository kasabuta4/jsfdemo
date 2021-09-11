package com.gmail.kasabuta4.jsfdemo.common.profile.dataaccess;

import com.gmail.kasabuta4.jsfdemo.config.jms.DataAccessProfileQueue;
import java.sql.Timestamp;
import java.time.Instant;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.jms.Destination;
import javax.jms.JMSContext;

@ProfilingDataAccess
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class DataAccessProfileIntercepter {

  @Inject JMSContext jmsContext;

  @Inject @DataAccessProfileQueue Destination profileQueue;

  @AroundInvoke
  public Object intercept(InvocationContext ic) throws Exception {
    Timestamp processStarted = Timestamp.from(Instant.now());

    Object result = ic.proceed();

    Timestamp processFinished = Timestamp.from(Instant.now());

    DataAccessProfile profile = new DataAccessProfile();
    profile.setTargetClass(ic.getMethod().getDeclaringClass().getName());
    profile.setTargetMethod(ic.getMethod().getName());
    profile.setProcessStarted(processStarted);
    profile.setProcessFinished(processFinished);
    jmsContext.createProducer().send(profileQueue, profile);

    return result;
  }
}
