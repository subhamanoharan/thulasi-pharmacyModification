package com.cgarcia.pillreminder.domain;

import java.io.Serializable;
import java.util.List;

public class Treatment implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -1567502553043154247L;

	protected String _name;

	protected List<Drug> _drugs;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public List<Drug> getDrugs() {
		return _drugs;
	}

	public void setDrugs(List<Drug> drugs) {
		_drugs = drugs;
	}

}
