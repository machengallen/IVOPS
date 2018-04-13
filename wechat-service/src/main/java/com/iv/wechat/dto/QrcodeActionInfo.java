package com.iv.wechat.dto;


public class QrcodeActionInfo {

	private QrcodeScene scene;

	public QrcodeActionInfo(QrcodeScene scene) {
		super();
		this.scene = scene;
	}

	public QrcodeActionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QrcodeScene getScene() {
		return scene;
	}

	public void setScene(QrcodeScene scene) {
		this.scene = scene;
	}
	
}
