package com.java.pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RenewalMain {
	// private static final String FILENAME = "Pipe Delimited PHI Clear One Group Example.txt";
	// private static final String FILENAME =
	// "Pipe Delimited PHI Clear Multi Group Example.txt";
	private static final String FILENAME = "Renewal Test File Clean .txt";

	private static final String BROKER_STARTER = "|||||||||||||||||||||||||||||||||||||||Broker|600001||";
	private static final String GROUP_TITLE = "|||||||||||||||||||||||||||||||||||||||600202||";
	private static final String PRIMARY_CONTACT = "Primary Contact||||||||||||||||||||||||||||||||||||||||600510||";
	private static final String BROKER_CONTACT = "Secondary/Broker Contact||||||||||||||||||||||||||||||||||||||||600510||";
	private static final String BILLING_CONTACT = "Billing Contact||||||||||||||||||||||||||||||||||||||||600510||";
	private static final String RATE_CHANGE_SUMMARY_REPORT = "|||||||||||||||||||||||||||||||||||||||RateChangeSummary|600001||";
	private static final String RATE_CHANGE_SUMMARY_REPORT_SUFFIX = "|||||||||||||||||||||||||||||||||||||600503||";
	private static final String PLAN_HMO = "Traditional HMO Plans||||||||||||||||||||||||||||||||||||||||600003||";
	private static final String PLAN_DHMO = "Ded/Coinsurance HMO Plans||||||||||||||||||||||||||||||||||||||||600003||";
	private static final String PLAN_HDHP = "High Deductible Health Plans||||||||||||||||||||||||||||||||||||||||600003||";
	private static final String PLAN_POS = "Point of Service Plans||||||||||||||||||||||||||||||||||||||||600003||";
	private static final String PLAN_STATEMANDATE = "State Mandated Plans||||||||||||||||||||||||||||||||||||||||600003||";
	private static final String PLAN_STARTER = "|||||||||||||||||||||||||||||||||||600204||";
	private static final String PLAN_PREMIUM = "|||||||||||||||||||||||||||||||600205||";
	private static final String SUPPLEMENTAL_COVERAGES = "Supplemental Coverages||||||||||||||||||||||||||||||||||||||||600003||";
	

	private static List<Renewal> listGroupArr = new ArrayList<Renewal>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readTxtFile(RenewalMain.FILENAME);

		printGroups();
	}

	public static void printGroups() {
		int index = 0;
		for (Renewal renewal : RenewalMain.listGroupArr) {
			index++;
			String strGroupName = renewal.getGroupName();
			String strRenewalDate = renewal.getRenewalDate();
			String strGroupID = renewal.getGroupID();
			String strAccountExecutive = renewal.getAccountExecutive();

			if (index < 10) {
				System.out.println("Group Name: " + strGroupName);
				System.out.println("Renewal Date: " + strRenewalDate);
				System.out.println("Group ID: " + strGroupID);
				System.out
						.println("Account Executive: " + strAccountExecutive);
				
				System.out.println("----------------");
				List<Contact> contacts = renewal.getContacts();
				for (Contact contact : contacts) { // print contact
					System.out.println(contact.getContactType());
					System.out.println(contact.getName());
					System.out.println(contact.getTitle());
					System.out.println(contact.getAddress1());
					System.out.println(contact.getAddress2());
					System.out.println(contact.getCity() + "  "
							+ contact.getState() + "  " + contact.getZip());
					System.out.println(contact.getPhone() + "  "
							+ contact.getFax());
					System.out.println(contact.getEmail());
					System.out.println("...........");
				}
				
				// print Rate Change Summary Report
				System.out.println("----------------");
				RateChangeSummary rcs = renewal.getRateChangeSummary();
				if (rcs != null) {
					System.out.println("Description           " + rcs.getDate_Old() + "    " + rcs.getDate_New() + "    Change");
					System.out.println("Plan Code             " + rcs.getPlanCode_Old() + "    " + rcs.getPlanCode_New());
					System.out.println("Index / Plan          " + rcs.getIndexPlan_Old() + "    " + rcs.getIndexPlan_New() + "    " + rcs.getIndexPlan_Change());
					System.out.println("Area                  " + rcs.getArea_Old() + "    " + rcs.getArea_New() + "    " + rcs.getArea_Change());
					System.out.println("Demographic           " + rcs.getDemographic_Old() + "    " + rcs.getDemographic_New() + "    " + rcs.getDemographic_Change());
					System.out.println("Rate / Adjustment     " + rcs.getRateAdjustment_Old() + "    " + rcs.getRateAdjustment_New() + "    " + rcs.getRateAdjustment_Change());
					System.out.println("Additional Ajustment  " + rcs.getRateAdjustment_Old() + "    " + rcs.getRateAdjustment_New() + "    " + rcs.getRateAdjustment_Change());
					System.out.println("Age Band Equivlence   " + rcs.getAgeBandEquivalence_Old() + "    " + rcs.getAgeBandEquivalence_New() + "    " + rcs.getAgeBandEquivalence_Change());
					System.out.println("Base Medical Total    " + rcs.getBaseMedicalTotal_Old() + "    " + rcs.getBaseMedicalTotal_New() + "    " + rcs.getBaseMedicalTotal_Change());
					System.out.println("Supplemental Benefits " + rcs.getSupplementalBenefits_Old() + "    " + rcs.getSupplementalBenefits_New() + "    " + rcs.getSupplementalBenefits_Change());
				} 
				
				// print Plans
				System.out.println("----------------");
				List<Plan> plans = renewal.getPlan();
				for (Plan plan : plans) {
					System.out.println(plan.getCatergory());
					System.out.println(plan.getName());
					System.out.println(plan.getMonthlyPremium());
					List<PlanPremium> planPremiums = plan.getPlanPremiums();
					for (PlanPremium planPremium : planPremiums) {
						System.out.println(planPremium.getAges() + "  " + planPremium.getSubscriber() + "  " + planPremium.getSubscriberSpouse() + "  " + planPremium.getSubscriberChild() + "  " + planPremium.getSubscriberFamily());
					}
				}

				System.out.println("==============================" + index);
			}
		}
	}

	public static void readTxtFile(String filePath) {
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				int titleFlag = 0; // Group Name/Renewal Date/Group ID
				Renewal renewal = null;
				Contact contact = null;
				List<Contact> contacts = null;
				RateChangeSummary rateChangeSummary = null;
				String strPlanCatergory = null;
				Plan plan1 = null;
				Plan plan2 = null;
				List<Plan> plans = null;
				int planFlag = 0;
				int planPremiumFlag = 0;
				PlanPremium planPremium = null;
				List<PlanPremium> planPremiums1 = null;
				List<PlanPremium> planPremiums2 = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.equals(RenewalMain.BROKER_STARTER)) {
						titleFlag = 0;
						renewal = new Renewal();
						contacts = new ArrayList<Contact>();
						plans = new ArrayList<Plan>();
						continue;
					} else if (lineTxt.endsWith(RenewalMain.GROUP_TITLE)
							&& titleFlag < 2) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strEntry2 = strSplitArr[1];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						String strValue2 = strEntry2.substring(
								strEntry2.indexOf(":") + 1).trim();
						if (titleFlag == 0) {
							renewal.setGroupName(strValue1);
							renewal.setRenewalDate(strValue2);
						}
						if (titleFlag == 1) {
							renewal.setGroupID(strValue1);
							renewal.setAccountExecutive(strValue2);
							RenewalMain.listGroupArr.add(renewal);
						}

						titleFlag++;
						// parse contact start
					} else if (lineTxt.equals(RenewalMain.PRIMARY_CONTACT)) {
						contact = new Contact();
						contact.setContactType("Primary");
					} else if (lineTxt.equals(RenewalMain.BROKER_CONTACT)) {
						contact = new Contact();
						contact.setContactType("Secondary/Broker");
					} else if (lineTxt.equals(RenewalMain.BILLING_CONTACT)) {
						contact = new Contact();
						contact.setContactType("Billing");
					} else if (lineTxt.startsWith("Name:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						contact.setName(strValue1);
					} else if (lineTxt.startsWith("Title:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						contact.setTitle(strValue1);
					} else if (lineTxt.startsWith("Address 1:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						contact.setAddress1(strValue1);
					} else if (lineTxt.startsWith("Address 2:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						contact.setAddress2(strValue1);
					} else if (lineTxt.startsWith("City:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strSplitArr1[] = strEntry1.split("   ");
						String strEntry1_1 = strSplitArr1[0];
						String strEntry1_2 = strSplitArr1[1];
						String strEntry1_3 = strSplitArr1[2];
						String strValue1_1 = strEntry1_1.substring(
								strEntry1_1.indexOf(":") + 1).trim();
						String strValue1_2 = strEntry1_2.substring(
								strEntry1_2.indexOf(":") + 1).trim();
						String strValue1_3 = strEntry1_3.substring(
								strEntry1_3.indexOf(":") + 1).trim();
						contact.setCity(strValue1_1);
						contact.setState(strValue1_2);
						contact.setZip(strValue1_3);
					} else if (lineTxt.startsWith("Phone:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strSplitArr1[] = strEntry1.split("   ");
						String strEntry1_1 = strSplitArr1[0];
						String strEntry1_2 = strSplitArr1[1];
						String strValue1_1 = strEntry1_1.substring(
								strEntry1_1.indexOf(":") + 1).trim();
						String strValue1_2 = strEntry1_2.substring(
								strEntry1_2.indexOf(":") + 1).trim();
						contact.setPhone(strValue1_1);
						contact.setFax(strValue1_2);
					} else if (lineTxt.startsWith("Email:")) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strValue1 = strEntry1.substring(
								strEntry1.indexOf(":") + 1).trim();
						contact.setEmail(strValue1);
					}
					if (lineTxt.equals(RenewalMain.PRIMARY_CONTACT)
							|| lineTxt.equals(RenewalMain.BROKER_CONTACT)
							|| lineTxt.equals(RenewalMain.BILLING_CONTACT)) {
						contacts.add(contact);
						renewal.setContacts(contacts);
					} // parse contact end
						// parse Rate Change Summary Report
					if (lineTxt.equals(RenewalMain.RATE_CHANGE_SUMMARY_REPORT)) {
						rateChangeSummary = new RateChangeSummary();
					} else if (lineTxt
							.endsWith(RenewalMain.RATE_CHANGE_SUMMARY_REPORT_SUFFIX)) {
						if (lineTxt.startsWith("Description")) {
							String strSplitArr[] = lineTxt.split("\\|");
							String strEntry2 = strSplitArr[1];
							String strEntry3 = strSplitArr[2];
							rateChangeSummary.setDate_Old(strEntry2);
							rateChangeSummary.setDate_New(strEntry3);
						} else if (lineTxt.startsWith("Plan Code")) {
							String strSplitArr[] = lineTxt.split("\\|");
							String strEntry2 = strSplitArr[1];
							String strEntry3 = strSplitArr[2];
							rateChangeSummary.setPlanCode_Old(strEntry2);
							rateChangeSummary.setPlanCode_New(strEntry3);
						} else if (lineTxt.startsWith("Index / Plan")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setIndexPlan_Old(dValue2);
							rateChangeSummary.setIndexPlan_New(dValue3);
							rateChangeSummary
									.setIndexPlan_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Area")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setArea_Old(dValue2);
							rateChangeSummary.setArea_New(dValue3);
							rateChangeSummary
									.setIndexPlan_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Demographic")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setDemographic_Old(dValue2);
							rateChangeSummary.setDemographic_New(dValue3);
							rateChangeSummary
									.setDemographic_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Rate Adjustment")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setRateAdjustment_Old(dValue2);
							rateChangeSummary.setRateAdjustment_New(dValue3);
							rateChangeSummary
									.setRateAdjustment_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Additional Adjustment")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary
									.setAddtionalAjustment_Old(dValue2);
							rateChangeSummary
									.setAddtionalAjustment_New(dValue3);
							rateChangeSummary
									.setAddtionalAjustment_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Age Band Equivalence")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary
									.setAgeBandEquivalence_Old(dValue2);
							rateChangeSummary
									.setAddtionalAjustment_New(dValue3);
							rateChangeSummary
									.setAddtionalAjustment_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Base Medical Total")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setBaseMedicalTotal_Old(dValue2);
							rateChangeSummary.setBaseMedicalTotal_New(dValue3);
							rateChangeSummary
									.setBaseMedicalTotal_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Supplemental Benefits")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary
									.setSupplementalBenefits_Old(dValue2);
							rateChangeSummary
									.setSupplementalBenefits_New(dValue3);
							rateChangeSummary
									.setSupplementalBenefits_Change((dValue3 - dValue2)
											/ dValue2);
						} else if (lineTxt.startsWith("Total Rate")) {
							String strSplitArr[] = lineTxt.split("\\|");
							double dValue2 = Double.parseDouble(strSplitArr[1]
									.trim());
							double dValue3 = Double.parseDouble(strSplitArr[2]
									.trim());
							rateChangeSummary.setTotalRate_Old(dValue2);
							rateChangeSummary.setTotalRate_New(dValue3);
							rateChangeSummary
									.setTotalRate_Change((dValue3 - dValue2)
											/ dValue2);
							
							renewal.setRateChangeSummary(rateChangeSummary);
						} // parse Rate Change Summary Report end
					}
					// parse Plans start
					if (lineTxt.equals(RenewalMain.PLAN_HMO)) {
						strPlanCatergory = "Traditional HMO Plans";
					} else if (lineTxt.equals(RenewalMain.PLAN_DHMO)) {
						strPlanCatergory = "Ded/Coinsurance HMO Plans";
					} else if (lineTxt.equals(RenewalMain.PLAN_HDHP)) {
						strPlanCatergory = "High Deductible Health Plans";
					} else if (lineTxt.equals(RenewalMain.PLAN_POS)) {
						strPlanCatergory = "Point of Service Plans";
					} else if (lineTxt.equals(RenewalMain.PLAN_STATEMANDATE)) {
						strPlanCatergory = "State Mandated Plans";
					} else if (lineTxt.endsWith(RenewalMain.PLAN_STARTER) 
							&& strPlanCatergory != null) {
						if (planFlag == 0) {
							String strSplitArr[] = lineTxt.split("\\|");
							String strEntry1 = strSplitArr[0];
							String strEntry2 = strSplitArr[5];
							plan1 = new Plan();
							plan1.setCatergory(strPlanCatergory);
							plan1.setName(strEntry1);
							planPremiums1 = new ArrayList<PlanPremium>();
							if (!"".equals(strEntry2)) {
								plan2 = new Plan();
								plan2.setCatergory(strPlanCatergory);
								plan2.setName(strEntry2);
								planPremiums2 = new ArrayList<PlanPremium>();
							}
							planFlag ++;
						} else if (planFlag == 1) {
							String strSplitArr[] = lineTxt.split("\\|");
							String strEntry1 = strSplitArr[0];
							String strEntry2 = strSplitArr[5];
							String strValue1 = strEntry1.substring(
									strEntry1.indexOf(":") + 1).trim();
							plan1.setMonthlyPremium(strValue1);
							if (!"".equals(strEntry2)) {
								String strValue2 = strEntry2.substring(
									strEntry2.indexOf(":") + 1).trim();
								plan2.setMonthlyPremium(strValue2);
							}
							planFlag = 0;
						}
					} else if (lineTxt.endsWith(RenewalMain.PLAN_PREMIUM) 
							&& lineTxt.indexOf("Subscriber") < 0 && strPlanCatergory != null) {
						String strSplitArr[] = lineTxt.split("\\|");
						String strEntry1 = strSplitArr[0];
						String strEntry2 = strSplitArr[1];
						String strEntry3 = strSplitArr[2];
						String strEntry4 = strSplitArr[3];
						String strEntry5 = strSplitArr[4];
						String strEntry6 = strSplitArr[5];
						String strEntry7 = strSplitArr[6];
						String strEntry8 = strSplitArr[7];
						String strEntry9 = strSplitArr[8];
						String strEntry10 = strSplitArr[9];
						
						planPremium = new PlanPremium();
						planPremium.setAges(strEntry1);
						planPremium.setSubscriber(strEntry2);
						planPremium.setSubscriberSpouse(strEntry3);
						planPremium.setSubscriberChild(strEntry4);
						planPremium.setSubscriberFamily(strEntry5);
						planPremiums1.add(planPremium);
						plan1.setPlanPremiums(planPremiums1);
						
						if (!"".equals(strEntry6)) {
							planPremium = new PlanPremium();
							planPremium.setAges(strEntry6);
							planPremium.setSubscriber(strEntry7);
							planPremium.setSubscriberSpouse(strEntry8);
							planPremium.setSubscriberChild(strEntry9);
							planPremium.setSubscriberFamily(strEntry10);
							planPremiums2.add(planPremium);
							plan2.setPlanPremiums(planPremiums2);
						}
						
						planPremiumFlag ++;
						if ((planPremiumFlag == 10 && !strEntry1.startsWith("All")) 
								|| (planPremiumFlag == 1 && strEntry1.startsWith("All"))) {
							plans.add(plan1);
							if (plan2 != null) plans.add(plan2);
							planPremiumFlag = 0;
						}
					}// parse Plan end
					// file end
					if (lineTxt.equals(RenewalMain.SUPPLEMENTAL_COVERAGES)) {
						renewal.setPlan(plans);
						strPlanCatergory = null;
					}
				}
				read.close();
			} else {
				System.out.println("Can't find file.");
			}
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}

	}

} //end
