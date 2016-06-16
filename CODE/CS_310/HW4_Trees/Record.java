import java.util.Comparator;

// Immutable.  Stores 3 strings referred to as entity, relation, and
// property. Each Record has a unique integer ID which is set on
// creation.  All records are made through the factory method
// Record.makeRecord(e,r,p).  Record which have some fields wild are
// created using Record.makeQuery(wild,e,r,p)
public class Record{
	private String entity, relation, property;
	private String wild = null;
	private int id;
	private static int makeId = 0;

	// Return the next ID that will be assigned to a Record on a call to
	// makeRecord() or makeQuery()
	public static int nextId(){
		return makeId+1;
	}

	// Return a stringy representation of the record. Each string should
	// be RIGHT justified in a field of 8 characters with whitespace
	// padding the left.  Java's String.format() is useful for padding
	// on the left.
	public String toString(){
		String ent = String.format("%8s ", entity);
		String rel = String.format("%8s ", relation);
		String pro = String.format("%8s ", property);
		return ent+rel+pro;
	}

	// Return true if this Record matches the parameter record r and
	// false otherwise. Two records match if all their fields match.
	// Two fields match if the fields are identical or at least one of
	// the fields is wild.
	public boolean matches(Record r){
		boolean match1 = true, match2=true, match3= true;
		//checks equality of fields
		match1 &= this.entity.equals(r.entity());
		match2 &= this.property.equals(r.property());
		match3 &= this.relation.equals(r.relation());
		//checks if either field is wild
		match1 |= this.entityWild();
		match1 |= r.entityWild();
		
		match2 |= this.propertyWild();
		match2 |= r.propertyWild();
		
		match3 |= this.relationWild();
		match3 |= r.relationWild();
		
		return match1&&match2&&match3;
	}
	public boolean equals(Record r){
		if (this.matches(r))
			return true;
		return false;
	}

	// Return this record's ID
	public int id(){
		return id;
	}

	// Accessor methods to access the 3 main fields of the record:
	// entity, relation, and property.
	public String entity(){
		return entity;
	}

	public String relation(){
		return relation;
	}

	public String property(){
		return property;
	}

	// Returns true/false based on whether the the three fields are
	// fixed or wild.
	public boolean entityWild(){
	//	if (wild == null)
	//		return false;
		if (entity.equals(wild))
			return true;
		return false;
	}

	public boolean relationWild(){
	//	if (wild == null)
	//		return false;
		if (relation.equals(wild))
			return true;
		return false;
	}

	public boolean propertyWild(){
		if (property.equals(wild))
			return true;
		return false;
	}

	// Factory method to create a Record. No public constructor is
	// required.
	public static Record makeRecord(String entity, String relation, String property){
		if ((entity==null)||(relation==null)||(property==null))
			throw new IllegalArgumentException();
		Record r = new Record();
		//sets values
		r.entity = entity;
		r.relation = relation;
		r.property = property;
		//r.wild = null;
		r.id = makeId;
		makeId++;
		return r;
	}
	
	// Create a record that has some fields wild. Any field that is
	// equal to the first argument wild will be a wild card
	public static Record makeQuery(String wild, String entity, String relation, String property){
		if ((entity==null)||(relation==null)||(property==null)
				||(wild==null))
			throw new IllegalArgumentException();
		Record r = new Record();
		//sets values
		r.entity = entity;
		r.relation = relation;
		r.property = property;
		r.wild = wild;
		r.id = makeId;
		makeId++;
		return r;
	}
	private static int compareEntity(Record r1, Record r2){
		int comp = 0;
		//compares wilds and values
		if (r2.entityWild()||r1.entityWild()||r1.entity().equals(r2.entity())){
			if (r1.entityWild())
				comp--;
			if (r2.entityWild())
				comp++;
			return comp;
		}
		comp = r1.entity().compareTo(r2.entity());
		return comp;
	}
	private static int compareRelation(Record r1, Record r2){
		int comp = 0;
		//compares wilds and values
		if (r2.relationWild()||r1.relationWild()||r1.relation().equals(r2.relation())){
			if (r1.relationWild())
				comp--;
			if (r2.relationWild())
				comp++;
			return comp;
		}
		comp = r1.relation().compareTo(r2.relation());
		return comp;
	}
	private static int compareProperty(Record r1, Record r2){
		int comp = 0;
		//compares wilds and values
		if (r2.propertyWild()||r1.propertyWild()||r1.property().equals(r2.property())){
			if (r1.propertyWild())
				comp--;
			if (r2.propertyWild())
				comp++;
			return comp;
		}
		comp = r1.property().compareTo(r2.property());
		return comp;
	}
	public static final Comparator<Record> ERPCompare = new Comparator<Record>(){
		public int compare(Record r1, Record r2) {
			int comp = compareEntity(r1, r2);//r1.entity().compareTo(r2.entity());
			if (comp!=0)
				return comp;
			comp = compareRelation(r1, r2);//r1.relation().compareTo(r2.relation());
			if (comp != 0)
				return comp;
			comp = compareProperty(r1, r2);//r1.property().compareTo(r2.property());
				return comp;
		}
		
	};
	public static final Comparator<Record> RPECompare = new Comparator<Record>(){
		public int compare(Record r1, Record r2) {
			int comp = compareRelation(r1, r2);//r1.relation().compareTo(r2.relation());
			if (comp != 0)
				return comp;
			comp = compareProperty(r1, r2);//r1.property().compareTo(r2.property());
			if (comp != 0)
				return comp;
			comp = compareEntity(r1, r2);//r1.entity().compareTo(r2.entity());
				return comp;	
		}
	};
	public static final Comparator<Record> PERCompare = new Comparator<Record>(){
		public int compare(Record r1, Record r2) {
			int comp = compareProperty(r1, r2);//r1.property().compareTo(r2.property());
			if (comp != 0)
				return comp;
			comp = compareEntity(r1, r2);//r1.entity().compareTo(r2.entity());
			if (comp != 0)
				return comp;
			comp = compareRelation(r1, r2);//r1.relation().compareTo(r2.relation());
			return comp;	
		}
	};
}