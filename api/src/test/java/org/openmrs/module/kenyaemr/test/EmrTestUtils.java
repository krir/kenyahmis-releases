/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyaemr.test;

import org.junit.Assert;
import org.junit.Ignore;
import org.openmrs.*;
import org.openmrs.api.OrderContext;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.module.kenyaemr.regimen.RegimenOrder;

import java.util.*;

/**
 * Utility methods for unit tests
 */

public class EmrTestUtils {

	/**
	 * Saves a regimen order
	 * @param patient the patient
	 * @param concepts the drug concepts
	 * @param start the start date
	 * @param end the end date
	 * @return the drug order
	 */
	public static RegimenOrder saveRegimenOrder(Patient patient, Collection<Concept> concepts, Date start, Date end) {
		Set<DrugOrder> orders = new LinkedHashSet<DrugOrder>();
		CareSetting outpatient = Context.getOrderService().getCareSettingByName("OUTPATIENT");
		OrderType drugOrderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);

		for (Concept concept : concepts) {
			/*DrugOrder order = new DrugOrder();
			order.setOrderType(Context.getOrderService().getOrderType(2));
			order.setPatient(patient);
			order.setOrderer(Context.getUserService().getUser(1));
			order.setConcept(concept);
			order.setDateActivated(start);
			order.setDiscontinued(end != null);
			order.setAction(end);
			orders.add((DrugOrder) Context.getOrderService().saveOrder(order));
*/

			DrugOrder order = new DrugOrder();
			order.setPatient(patient);
			List<Provider> provider = (List<Provider>) Context.getProviderService().getProvidersByPerson(Context.getUserService().getUser(1).getPerson());
			Encounter e = Context.getEncounterService().getEncounter(3);
			order.setEncounter(e);
			order.setOrderer(provider.get(0));
			order.setConcept(concept);
			order.setDateActivated(start);
			order.setDose(2.0);
			order.setDoseUnits(Context.getConceptService().getConcept(51));
			order.setRoute(Context.getConceptService().getConcept(22));
			OrderFrequency orderFrequency = Context.getOrderService().getOrderFrequency(1);
			order.setFrequency(orderFrequency);


			if (end != null) {
				order.setAction(Order.Action.DISCONTINUE);
			}

			OrderContext orderContext = new OrderContext();
			orderContext.setCareSetting(outpatient);
			orderContext.setOrderType(drugOrderType);
		}
		return new RegimenOrder(orders);
	}

	/**
	 * Saves a regimen order
	 * @param patient the patient
	 * @param concepts the drug concepts
	 * @param start the start date
	 * @param end the end date
	 * @return the drug order
	 */
	public static RegimenOrder saveRegimenOrder(Patient patient, Collection<Concept> concepts, Date start, Date end, Encounter encounter) {
		Set<DrugOrder> orders = new LinkedHashSet<DrugOrder>();
		CareSetting outpatient = Context.getOrderService().getCareSettingByName("OUTPATIENT");
		OrderType drugOrderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);

		for (Concept concept : concepts) {
			/*DrugOrder order = new DrugOrder();
			order.setOrderType(Context.getOrderService().getOrderType(2));
			order.setPatient(patient);
			order.setOrderer(Context.getUserService().getUser(1));
			order.setConcept(concept);
			order.setDateActivated(start);
			order.setDiscontinued(end != null);
			order.setAction(end);
			orders.add((DrugOrder) Context.getOrderService().saveOrder(order));
*/

			DrugOrder order = new DrugOrder();
			order.setPatient(patient);
			List<Provider> provider = (List<Provider>) Context.getProviderService().getProvidersByPerson(Context.getUserService().getUser(1).getPerson());
			order.setEncounter(encounter);
			order.setOrderer(provider.get(0));
			order.setConcept(concept);
			order.setDateActivated(start);
			order.setDose(2.0);
			order.setDoseUnits(Context.getConceptService().getConcept(51));
			order.setRoute(Context.getConceptService().getConcept(22));
			OrderFrequency orderFrequency = Context.getOrderService().getOrderFrequency(1);
			order.setFrequency(orderFrequency);


			if (end != null) {
				order.setAction(Order.Action.DISCONTINUE);
			}

			OrderContext orderContext = new OrderContext();
			orderContext.setCareSetting(outpatient);
			orderContext.setOrderType(drugOrderType);
			orders.add((DrugOrder) Context.getOrderService().saveOrder(order, orderContext));
		}
		return new RegimenOrder(orders);
	}

	/**
	 * Asserts that the given regimen contains only the given drug orders
	 * @param reg
	 * @param drugOrders
	 */
	public static void assertRegimenContainsDrugOrders(RegimenOrder reg, DrugOrder... drugOrders) {
		Assert.assertEquals(drugOrders.length, reg.getDrugOrders().size());
		for (DrugOrder o : drugOrders) {
			Assert.assertTrue(reg.getDrugOrders().contains(o));
		}
	}

	/**
	 * Creates a calculation context
	 * @param now the now date
	 * @return the context
	 */
	public static PatientCalculationContext calculationContext(Date now) {
		PatientCalculationContext context = Context.getService(PatientCalculationService.class).createCalculationContext();
		context.setNow(now);
		return context;
	}
}