package com.midas.app;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.providers.external.stripe.StripeConfiguration;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.workflows.CreateAccountWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class MidasApplication {

  public static void main(String[] args) {
    SpringApplication.run(MidasApplication.class, args);
  }

  @Configuration
  static class TemporalConfiguration {

    @Autowired private WorkflowClient workflowClient;

    @Autowired private StripeConfiguration stripeConfiguration;

    @Bean
    public StripePaymentProvider stripePaymentProvider() {
      return new StripePaymentProvider(stripeConfiguration);
    }

    @Bean
    public WorkerFactory workerFactory() {
      return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker createAccountWorker(
        WorkerFactory workerFactory, StripePaymentProvider stripePaymentProvider) {
      Worker worker = workerFactory.newWorker(CreateAccountWorkflowImpl.QUEUE_NAME);
      worker.registerWorkflowImplementationTypes(CreateAccountWorkflowImpl.class);
      worker.registerActivitiesImplementations(new AccountActivityImpl(stripePaymentProvider));
      return worker;
    }

    @Bean
    public WorkflowClient workflowClient() {
      WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
      return WorkflowClient.newInstance(service);
    }
  }
}
