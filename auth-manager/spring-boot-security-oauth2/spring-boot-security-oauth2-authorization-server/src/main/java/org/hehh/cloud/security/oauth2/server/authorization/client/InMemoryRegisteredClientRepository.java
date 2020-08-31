package org.hehh.cloud.security.oauth2.server.authorization.client;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository} that stores {@link RegisteredClient}(s) in-memory.
 *  原生的InMemoryRegisteredClientRepository 实现 可操作的存储库
 * @author Anoop Garlapati
 * @see RegisteredClientRepository
 * @see RegisteredClient
 * @date: 2020-08-29 22:50
 * @since 0.0.1
 */
public class InMemoryRegisteredClientRepository implements org.hehh.cloud.security.oauth2.server.authorization.client.RegisteredClientRepository {

    private final Map<String, RegisteredClient> idRegistrationMap;
    private final Map<String, RegisteredClient> clientIdRegistrationMap;





    /**
     * Constructs an {@code InMemoryRegisteredClientRepository} using the provided parameters.
     *
     * @param registrations the client registration(s)
     */
    public InMemoryRegisteredClientRepository(RegisteredClient... registrations) {
        this(Arrays.asList(registrations));
    }

    /**
     * Constructs an {@code InMemoryRegisteredClientRepository} using the provided parameters.
     *
     * @param registrations the client registration(s)
     */
    public InMemoryRegisteredClientRepository(List<RegisteredClient> registrations) {
        Assert.notEmpty(registrations, "registrations cannot be empty");
        ConcurrentHashMap<String, RegisteredClient> idRegistrationMapResult = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, RegisteredClient> clientIdRegistrationMapResult = new ConcurrentHashMap<>();
        for (RegisteredClient registration : registrations) {
            Assert.notNull(registration, "registration cannot be null");
            String id = registration.getId();
            if (idRegistrationMapResult.containsKey(id)) {
                throw new IllegalArgumentException("Registered client must be unique. " +
                    "Found duplicate identifier: " + id);
            }
            String clientId = registration.getClientId();
            if (clientIdRegistrationMapResult.containsKey(clientId)) {
                throw new IllegalArgumentException("Registered client must be unique. " +
                    "Found duplicate client identifier: " + clientId);
            }
            idRegistrationMapResult.put(id, registration);
            clientIdRegistrationMapResult.put(clientId, registration);
        }
        this.idRegistrationMap = idRegistrationMapResult;
        this.clientIdRegistrationMap = clientIdRegistrationMapResult;
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.idRegistrationMap.get(id);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.clientIdRegistrationMap.get(clientId);
    }


    /**
     * Delete the registered client identified by the provided {@code Id}
     *
     * @param id id
     */
    @Override
    public void deleteById(String id) {
        RegisteredClient client = this.findById(id);
        if(client != null){
            this.idRegistrationMap.remove(id);
            this.clientIdRegistrationMap.remove(client.getClientId());
        }
    }

    /**
     * Delete the registered client identified by the provided {@code clientId}.
     *
     * @param clientId 客户机id
     */
    @Override
    public void deleteByClientId(String clientId) {
        RegisteredClient client = this.findByClientId(clientId);
        if(client != null){
            this.idRegistrationMap.remove(client.getId());
            this.clientIdRegistrationMap.remove(clientId);
        }
    }
}
