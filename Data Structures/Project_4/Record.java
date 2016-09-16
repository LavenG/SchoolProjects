

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
		if (this.inspectionDate != null && this.gradeDate != null && this.recordDate != null) {
			return "\"" + this.camis + "\", " + "\"" + this.dba + "\", " + "\"" + this.boro + "\", " + "\""
					+ this.building + "\", " + "\"" + this.street + "\", " + "\"" + this.zipcode + "\", " + "\""
					+ this.phone + "\", " + "\"" + this.cuisineDescription + "\", " + "\""
					+ dateFormat.format(this.inspectionDate) + "\", " + "\"" + this.action + "\", " + "\""
					+ this.violationCode + "\", " + "\"" + this.violationDescription + "\", " + "\"" + this.criticalFlag
					+ "\", " + "\"" + this.score + "\", " + "\"" + this.grade + "\", " + "\""
					+ dateFormat.format(this.gradeDate) + "\", " + "\"" + dateFormat.format(this.recordDate) + "\", "
					+ "\"" + this.inspectionType + "\", ";
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
		if (this.inspectionDate != null) {
			return String.format(
					"%10.10s\t%20.20s\t%20.20s\t%20.20s\t" + dateFormat.format(this.inspectionDate) + "\t%3d",
					this.camis, this.dba, this.getAddress(), this.cuisineDescription, this.score);
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

		return String.format(this.building + ", " + this.street);
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
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
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
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
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
		if (compareResult == 0) {
			int camisResults = arg0.getCamis().compareTo(arg1.getCamis());
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
		if (compareResult == 0) {
			return arg0.getCamis().compareTo(arg1.getCamis());
		} else
			return compareResult;
	}
}
