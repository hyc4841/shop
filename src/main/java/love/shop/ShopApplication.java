package love.shop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.webhook.WebhookVerifier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import love.shop.common.configuration.PortOneSecretProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching // 캐시 활성화
@SpringBootApplication
@EnableConfigurationProperties(PortOneSecretProperties.class)
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	@PersistenceContext
	EntityManager em;
	@Bean
	JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}

	@Bean
	PaymentClient paymentClient(PortOneSecretProperties secret) {
		return new PaymentClient(secret.getApi(), "https://api.portone.io", secret.getStoreId());
	}

	@Bean
	WebhookVerifier webhookVerifier(PortOneSecretProperties secret) {
		return new WebhookVerifier(secret.getWebhook());
	}

	/*
	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		return new Hibernate5JakartaModule();
	}
	 */

}
