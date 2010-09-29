/**
 *
 */
package edu.wustl.idp;

import java.util.List;
import java.util.Properties;

/**
 * @author supriya_dankh
 *
 */
public class AbstractBaseIDP implements IDPInterface {
	private String name;
	private Properties userProperties;
	private Properties idpProperties;
	private List<Properties> migrationProperties;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Properties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(final Properties userProperties) {
		this.userProperties = userProperties;
	}

	public Properties getIDPProperties() {
		return this.idpProperties;
	}

	public List<Properties> getMigrationProperties() {
		return this.migrationProperties;

	}

	public void setIDPProperties(Properties idpProperties) {
		this.idpProperties = idpProperties;
	}

	public void setMigrationProperties(List<Properties> migrationProperties) {

		this.migrationProperties = migrationProperties;
	}

}