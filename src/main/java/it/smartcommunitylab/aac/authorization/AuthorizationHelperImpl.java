package it.smartcommunitylab.aac.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.RequestedAuthorization;

public class AuthorizationHelperImpl implements AuthorizationHelper {

	private final static Logger logger = LoggerFactory.getLogger(AuthorizationHelperImpl.class);

	@Autowired
	private AuthorizationSchemaHelper authSchema;

	@Autowired
	private AuthorizationStorage storage;

	@Override
	public Authorization insert(Authorization auth) throws NotValidResourceException {

		boolean isValidResource = authSchema.isValid(auth.getResource());
		if (isValidResource) {
			auth = storage.insert(auth);
			logger.info("inserted authorization: {}", auth);
			return auth;
		} else {
			logger.warn("tried to insert a not valid authorization: {}", auth);
			throw new NotValidResourceException("resource in authorization is not valid");
		}
	}

	@Override
	public void remove(Authorization auth) {
		storage.remove(auth);
		logger.info("removed authorization: {}", auth);

	}

	@Override
	public boolean validate(RequestedAuthorization auth) {
		boolean isAuthGranted = storage.search(new Authorization(auth));
		if (isAuthGranted) {
			logger.info("authorization is granted: {}", auth);
		} else {
			logger.info("authorization is not granted: {}", auth);
		}
		return isAuthGranted;
	}

	@Override
	public void remove(String authorizationId) {
		storage.remove(authorizationId);
		logger.info("removed authorization id: {}", authorizationId);

	}

}
