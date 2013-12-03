package com.brotherlogic.proxycache.discogs;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.proxycache.ObjectManager;
import com.brotherlogic.proxycache.runners.DiscogsService;

public class IdentityTest {

	@Test
	public void retrieveIdentity() throws IOException {
		DiscogsService.forceCache(true);
		DiscogsService service = new DiscogsService();
		ObjectManager<Identity> manager = new ObjectManager<Identity>(
				Identity.class, service);
		Identity ident = manager.get();
		Assert.assertEquals("Mismatch in identity names", "BrotherLogic",
				ident.getUsername());
	}

}
