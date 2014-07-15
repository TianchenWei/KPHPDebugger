package org.phpsemantics.debug.core.launching;

import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

public class KPHPSourceLookupDirector extends AbstractSourceLookupDirector {

	@Override
	public void initializeParticipants() {
		// TODO Auto-generated method stub
	    addParticipants(new ISourceLookupParticipant[]{new KPHPSourceLookupParticipant()});

	}

}
