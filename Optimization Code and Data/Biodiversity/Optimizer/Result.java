
public class Result {

	String parameters;
	public float correct;
	public float ambiguous;
	public float incorrect;
	public float redundant;
	
	public Result(String parameters, float correct, float ambiguous, float incorrect, float redundant){
		this.parameters = parameters;
		this.correct = correct;
		this.ambiguous = ambiguous;
		this.incorrect = incorrect;
		this.redundant = redundant;
	}
	
	public boolean lessThan(Result other){
		if (this.correct + this.ambiguous < other.correct + other.ambiguous){
			return true;
		}
		else if (this.correct + this.ambiguous == other.correct + other.ambiguous){
			if (this.correct < other.correct){
				return true;
			}
			else if (this.correct == other.correct){
				if (this.ambiguous < other.ambiguous){
					return true;
				}
				else if (this.ambiguous == other.ambiguous){
					if (this.incorrect > other.incorrect){
						return true;
					}
					else if (this.incorrect == other.incorrect){
						if (this.redundant > other.redundant){
							return true;
						}
						else return false;
					}
					else return false;
				}
				else return false;
			}
			else return false;
		}
		else return false;
	}
	
	public String toString(){
		return parameters + "," + correct + "," + ambiguous + "," + incorrect + "," + redundant; 
	}
}
