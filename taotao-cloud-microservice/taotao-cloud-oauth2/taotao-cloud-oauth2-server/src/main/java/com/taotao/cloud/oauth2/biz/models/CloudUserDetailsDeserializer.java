package com.taotao.cloud.oauth2.biz.models;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CloudUserDetailsDeserializer extends JsonDeserializer<CloudUserDetails> {

	@Override
	public CloudUserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);

		//final JsonNode authoritiesNode = readJsonNode(jsonNode, "authorities");
		//Set<String> userAuthorities = getUserAuthorities(mapper, authoritiesNode);
		
		Long id = readJsonNode(jsonNode, "id").asLong();
		String username = readJsonNode(jsonNode, "username").asText();
		String password = readJsonNode(jsonNode, "password").asText();
		boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();

		boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
		boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
		boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();

		Set<String> userAuthorities = new HashSet<>();
		userAuthorities.add("USER");
		userAuthorities.add("ADMIN");
		CloudUserDetails cloudUserDetails = CloudUserDetails.withId(id)
			.username(username)
			.password(password)
			.roles(userAuthorities.toArray(new String[userAuthorities.size()]))
			.accountNonLocked(!accountNonExpired)
			.enabled(enabled)
			.accountNonExpired(true)
			.credentialsNonExpired(true)

			.build();

		//if (passwordNode.asText(null) == null) {
			cloudUserDetails.eraseCredentials();
		//}
		
		return cloudUserDetails;
		
	}
	
	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}
	
	private Set<String> getUserAuthorities(final ObjectMapper mapper, final JsonNode authoritiesNode) throws JsonParseException, JsonMappingException, IOException {
		
		Set<String> userAuthorities = new HashSet<>();
		if (authoritiesNode != null) {
			if (authoritiesNode.isArray()) {
				for (final JsonNode objNode : authoritiesNode) {
					if (objNode.isArray()) {
						ArrayNode arrayNode = (ArrayNode) objNode;
						for (JsonNode elementNode : arrayNode) {
							String userAuthority = mapper.readValue(elementNode.traverse(mapper), String.class);
							userAuthorities.add(userAuthority);
						}
					} 
				}
			}
		}
		return userAuthorities;
	}

}
