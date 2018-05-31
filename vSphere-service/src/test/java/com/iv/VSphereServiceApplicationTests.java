package com.iv;

import java.net.URL;

import com.vmware.vim.rest.RestClient;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VSphereServiceApplicationTests {

	 public static void main(String[] args){  
	        try {  
	        	String ip="10.10.17.220";  
	            String username = "inno-view\\mac";
	            String password = "123@abcd";
	            URL url = new URL("https", ip, "/sdk");  
	            ServiceInstance si = new ServiceInstance(url, username, password, true);  
	            AboutInfo aboutInfo = si.getAboutInfo();
	            System.out.println(aboutInfo.getFullName());
	            System.out.println(aboutInfo.getLicenseProductName());
	            System.out.println(aboutInfo.getOsType());
	            System.out.println(aboutInfo.getApiType());
	            System.out.println(aboutInfo.getLocaleVersion());
	            System.out.println(aboutInfo.getVendor());
	            Folder rootFolder = si.getRootFolder();  
	            InventoryNavigator navigator = new InventoryNavigator(rootFolder);
	            ManagedEntity[] entities = navigator.searchManagedEntities("HostSystem");
	            for (ManagedEntity managedEntity : entities) {
	            	HostSystem hostSystem = (HostSystem)managedEntity;
	            	System.out.println("Host system name:" + hostSystem.getName());
				}
	            ManagedEntity mes = navigator.searchManagedEntity("VirtualMachine","ivops-db-10.10.31.25");  
	            if (mes != null) {  
	                VirtualMachine virtualMachine = (VirtualMachine) mes;  
	                System.out.println("VirtualMachine name:" + virtualMachine.getName());  
	                //重新加载  
	                //virtualMachine.reload();  
	                //虚拟机关机  
	                //virtualMachine.shutdownGuest();  
	                //虚拟机待机  
	                //virtualMachine.standbyGuest();  
	            }else{  
	                si.getServerConnection().logout();  
	            }  
	  
	  
	        }catch (Exception e){  
	           e.printStackTrace();  
	        }  
	  
	  
	        }  
}
