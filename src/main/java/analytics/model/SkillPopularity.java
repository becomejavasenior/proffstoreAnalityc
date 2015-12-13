package analytics.model;

public class SkillPopularity {

	private String skill;
	private long popularity;

	public SkillPopularity(String skill, long popularity) {
		this.skill = skill;
		this.popularity = popularity;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public long getPopularity() {
		return popularity;
	}

	public void setPopularity(long popularity) {
		this.popularity = popularity;
	}

}
