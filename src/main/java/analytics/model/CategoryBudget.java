package analytics.model;

public class CategoryBudget {

	private String category;
	private long budget;

	public CategoryBudget(String category, long budget) {
		this.category = category;
		this.budget = budget;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getBudget() {
		return budget;
	}

	public void setBudget(long budget) {
		this.budget = budget;
	}

}
