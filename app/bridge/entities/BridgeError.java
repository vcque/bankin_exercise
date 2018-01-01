package bridge.entities;

public class BridgeError extends RuntimeException {

	public String body;

	public int status;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BridgeError [body=" + body + ", status=" + status + "]";
	}

}
