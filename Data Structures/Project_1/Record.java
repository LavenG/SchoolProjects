package proj1;


/**
 *This record class represents a single inspection record which corresponds to a single line in the input file  
 * it stores the different variables provided by the file in its data fields
 * and provides comparators in order to compare different records.
 *
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Record {

	private String camis;
	private String dba;
	private String boro;
	private String building;
	private String street;
	private String zipcode;
	private String phone;
	private String cuisineDescription;
	private Date inspectionDate;
	private String action;
	private String violationCode;
	private String violationDescription;
	private String criticalFlag;
	private int score;
	private String grade;
	private Date gradeDate;
	private Date recordDate;
	private String inspectionType;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

	/**
	 * Constructs a Record object by initializing all its data fields by reading
	 * input from an ArrayList of Strings that is constructed by the main method
	 *
	 * @param entries
	 *            an ArrayList of Strings
	 *
	 */
	public Record(ArrayList<String> entries) {

		this.camis = entries.get(EntriesOrder.CAMIS.ordinal());
		this.dba = entries.get(EntriesOrder.DBA.ordinal());
		this.boro = entries.get(EntriesOrder.BORO.ordinal());
		this.building = entries.get(EntriesOrder.BUILDING.ordinal());
		this.street = entries.get(EntriesOrder.STREET.ordinal());
		this.zipcode = entries.get(EntriesOrder.ZIPCODE.ordinal());
		this.phone = entries.get(EntriesOrder.PHONE.ordinal());
		this.cuisineDescription = entries.get(EntriesOrder.CUSINE_DESCRIPTION.ordinal());
		// Check if the date is provided before trying to read it
		if (entries.get(EntriesOrder.INSPECTION_DATE.ordinal()) != null) {
			try {
				this.inspectionDate = dateFormat.parse(entries.get(EntriesOrder.INSPECTION_DATE.ordinal()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			this.inspectionDate = null;
		}

		this.action = entries.get(EntriesOrder.ACTION.ordinal());
		this.violationCode = entries.get(EntriesOrder.VIOLATION_CODE.ordinal());
		this.violationDescription = entries.get(EntriesOrder.VIOLATION_DESCRIPTION.ordinal());
		this.criticalFlag = entries.get(EntriesOrder.CRITICAL_FLAG.ordinal());
		// Check if the score is provided before trying to read it
		if (!entries.get(EntriesOrder.SCORE.ordinal()).equals("")) {
			try {
				this.score = Integer.parseInt(entries.get(EntriesOrder.SCORE.ordinal()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			this.score = -1;
		}
		this.grade = entries.get(EntriesOrder.GRADE.ordinal());
		this.grade = entries.get(EntriesOrder.GRADE_DATE.ordinal());
		// Check if the date is provided before trying to read it
		if (entries.get(EntriesOrder.RECORD_DATE.ordinal()) != null) {
			try {
				this.recordDate = dateFormat.parse(entries.get(EntriesOrder.RECORD_DATE.ordinal()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			this.recordDate = null;
		}

		this.inspectionType = entries.get(EntriesOrder.INSPECTION_TYPE.ordinal());
	}

	/**
	 * Returns each data field of a Record object in comma separated format.
	 * Makes sure that dates are only formatted when not null.
	 *
	 * @return a String object that gives all available information about a
	 *         Record object
	 */
	public String toString() {
		// if all the dates were provided format them all and print all data
		// fields
		if (this.inspectionDate != null && this.gradeDate != null && this.recordDate != null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\""
					+ dateFormat.format(this.inspectionDate) + "\", " + "\"" + this.action + "\", " + "\""
					+ this.violationCode + "\", " + "\"" + this.violationDescription + "\", " + "\"" + this.criticalFlag
					+ "\", " + "\"" + this.score + "\", " + "\"" + this.grade + "\", " + "\""
					+ dateFormat.format(this.gradeDate) + "\", " + "\"" + dateFormat.format(this.recordDate) + "\", "
					+ "\"" + this.inspectionType + "\", ";
			// Otherwise depending on which dates are provided and which are not
			// format the ones that are
			// and print all the data fields
		} else if (this.inspectionDate != null && this.gradeDate != null && this.recordDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\""
					+ dateFormat.format(this.inspectionDate) + "\", " + "\"" + this.action + "\", " + "\""
					+ this.violationCode + "\", " + "\"" + this.violationDescription + "\", " + "\"" + this.criticalFlag
					+ "\", " + "\"" + this.score + "\", " + "\"" + this.grade + "\", " + "\""
					+ dateFormat.format(this.gradeDate) + "\", " + "\"" + this.recordDate + "\", " + "\""
					+ this.inspectionType + "\", ";
		} else if (this.inspectionDate != null && this.gradeDate == null && this.recordDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\""
					+ dateFormat.format(this.inspectionDate) + "\", " + "\"" + this.action + "\", " + "\""
					+ this.violationCode + "\", " + "\"" + this.violationDescription + "\", " + "\"" + this.criticalFlag
					+ "\", " + "\"" + this.score + "\", " + "\"" + this.grade + "\", " + "\"" + this.gradeDate + "\", "
					+ "\"" + this.recordDate + "\", " + "\"" + this.inspectionType + "\", ";
		} else if (this.gradeDate != null && this.inspectionDate == null && this.recordDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\"" + this.inspectionDate
					+ "\", " + "\"" + this.action + "\", " + "\"" + this.violationCode + "\", " + "\""
					+ this.violationDescription + "\", " + "\"" + this.criticalFlag + "\", " + "\"" + this.score
					+ "\", " + "\"" + this.grade + "\", " + "\"" + dateFormat.format(this.gradeDate) + "\", " + "\""
					+ this.recordDate + "\", " + "\"" + this.inspectionType + "\", ";
		} else if (this.recordDate != null && this.inspectionDate == null && this.gradeDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\"" + this.inspectionDate
					+ "\", " + "\"" + this.action + "\", " + "\"" + this.violationCode + "\", " + "\""
					+ this.violationDescription + "\", " + "\"" + this.criticalFlag + "\", " + "\"" + this.score
					+ "\", " + "\"" + this.grade + "\", " + "\"" + this.gradeDate + "\", " + "\""
					+ dateFormat.format(this.recordDate) + "\", " + "\"" + this.inspectionType + "\", ";
		} else if (this.inspectionDate != null && this.recordDate != null && this.gradeDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\""
					+ dateFormat.format(this.inspectionDate) + "\", " + "\"" + this.action + "\", " + "\""
					+ this.violationCode + "\", " + "\"" + this.violationDescription + "\", " + "\"" + this.criticalFlag
					+ "\", " + "\"" + this.score + "\", " + "\"" + this.grade + "\", " + "\"" + this.gradeDate + "\", "
					+ "\"" + dateFormat.format(this.recordDate) + "\", " + "\"" + this.inspectionType + "\", ";
		} else if (this.gradeDate != null && this.recordDate != null && this.inspectionDate == null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\"" + this.inspectionDate
					+ "\", " + "\"" + this.action + "\", " + "\"" + this.violationCode + "\", " + "\""
					+ this.violationDescription + "\", " + "\"" + this.criticalFlag + "\", " + "\"" + this.score
					+ "\", " + "\"" + this.grade + "\", " + "\"" + dateFormat.format(this.gradeDate) + "\", " + "\""
					+ dateFormat.format(this.recordDate) + "\", " + "\"" + this.inspectionType + "\", ";
		} else {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\"" + this.inspectionDate
					+ "\", " + "\"" + this.action + "\", " + "\"" + this.violationCode + "\", " + "\""
					+ this.violationDescription + "\", " + "\"" + this.criticalFlag + "\", " + "\"" + this.score
					+ "\", " + "\"" + this.grade + "\", " + "\"" + this.gradeDate + "\", " + "\"" + this.recordDate
					+ "\", " + "\"" + this.inspectionType + "\", ";
		}

	}

	/**
	 * Formats desired information in the desired way as specified for the
	 * assignment. Makes sure that date is only formatted when not null
	 *
	 * @return a String object that specifies the Camis, Dba, Address, Cuisine,
	 *         Date, and score of a Record object
	 */
	public String toString6() {
		// if the inspection date is provided format it and print it out with
		// the rest of the desired data fields in the correct format
		if (this.inspectionDate != null) {
			return String.format(
					"%10.10s\t%20.20s\t%20.20s\t%20.20s\t" + dateFormat.format(this.inspectionDate) + "\t%3d",
					this.camis, this.dba, this.getAddress(), this.cuisineDescription, this.score);
			// if the inspection date is not provided then don't try to format
			// it and print out the rest of the desired data fields in the
			// correct format
		} else {
			return String.format("%10.10s\t%20.20s\t%20.20s\t%20.20s\t" + this.inspectionDate + "\t%3d", this.camis,
					this.dba, this.getAddress(), this.cuisineDescription, this.score);
		}

	}

	public String getDba() {

		return this.dba;
	}

	public Date getInspectionDate() {

		return this.inspectionDate;
	}

	public String getCamis() {

		return this.camis;
	}

	public String getCuisineDescription() {

		return this.cuisineDescription;
	}

	public int getScore() {

		return this.score;
	}

	public String getAddress() {

		return String.format(this.building + " " + this.street);
	}

	public String getZip() {
		return this.zipcode;
	}

}

/**
 * Defines Comparator object for the objects of type record. The objects are
 * compared by their unique camis number.
 * 
 * @author Joanna Klukowska
 *
 */
// Updated to compare by inspectionDate in case the Camis are equal
class RecordComparatorByCamis implements Comparator<Record> {
	public int compare(Record arg0, Record arg1) {
		int compareResult = arg0.getCamis().compareTo(arg1.getCamis());
		// if the Camis are the same then compare by inspection date to decide
		// the order
		if (compareResult == 0) {
			return arg0.getInspectionDate().compareTo(arg1.getInspectionDate());
		} else {
			return compareResult;
		}
	}
}

/**
 * Defines Comparator object for the objects of type record. The objects are
 * compared by the name of the business; ties are resolved based on the unique
 * camis number.
 * 
 * @author Joanna Klukowska
 *
 */
// Updated to compare by Camis if the DBAs are the same and then by
// inspectionDate
// in case the DBAs are the same
class RecordComparatorByDBA implements Comparator<Record> {

	public int compare(Record arg0, Record arg1) {
		int compareResult = arg0.getDba().compareToIgnoreCase(arg1.getDba());
		// If the DBAs are the same then compare by Camis
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
			// if the Camis are the same then compare by inspection date to
			// decide the order
			if (camisResults == 0) {
				int dateResults = arg0.getInspectionDate().compareTo(arg1.getInspectionDate());
				return dateResults;
			} else {
				return camisResults;
			}
		} else
			return compareResult;
	}
}

/**
 * Defines Comparator object for the objects of type record. The objects are
 * compared by the type of cuisine; ties are resolved based on the unique camis
 * number.
 * 
 * @author Joanna Klukowska
 *
 */
// Updated to compare by Camis if the Cuisines are the same and then by
// InspectionDate
// in case the Cuisines are the same
class RecordComparatorByCuisine implements Comparator<Record> {

	public int compare(Record arg0, Record arg1) {
		int compareResult = arg0.getCuisineDescription().compareToIgnoreCase(arg1.getCuisineDescription());
		// if the Cuisine Descriptions are the same then compare by Camis to
		// decide the order
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
			// If the Camis are the same then compare by Inspection date to
			// decide the order
			if (camisResults == 0) {
				int dateResults = arg0.getInspectionDate().compareTo(arg1.getInspectionDate());
				return dateResults;
			} else {
				return camisResults;
			}
		} else
			return compareResult;
	}
}

/**
 * Defines Comparator object for the objects of type record. The objects are
 * compared by inspection scores; ties are resolved based on the unique camis
 * number.
 * 
 * @author Joanna Klukowska
 *
 */
// Updated to compare by Camis if the Score is the same and then
// by inspectionDate in case the Camis are the same
class RecordComparatorByScore implements Comparator<Record> {

	public int compare(Record arg0, Record arg1) {
		int compareResult = arg0.getScore() - arg1.getScore();
		// if the scores are the same then compare by Camis to decide the order
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
			// if the camis are the same then compare by InspectionDate to
			// decide the order
			if (camisResults == 0) {
				int dateResults = arg0.getInspectionDate().compareTo(arg1.getInspectionDate());
				return dateResults;
			} else {
				return camisResults;
			}
		} else
			return compareResult;
	}
}

/**
 * Defines Comparator object for the objects of type record. The objects are
 * compared by inspection date; ties are resolved based on the unique camis
 * number.
 * 
 * @author Joanna Klukowska
 *
 */
// Updated to compare by Camis in case the Date is the same
class RecordComparatorByDate implements Comparator<Record> {

	public int compare(Record arg0, Record arg1) {
		int compareResult = arg0.getInspectionDate().compareTo(arg1.getInspectionDate());
		// If the inspection dates are the same then compare by Camis to decide
		// the order
		if (compareResult == 0) {
			return arg0.getCamis().compareTo(arg1.getCamis());
		} else
			return compareResult;
	}
}
