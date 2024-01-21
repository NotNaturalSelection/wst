package org.example;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3.client.transport.TransportException;
import org.example.client.ws.ServiceException;
import org.example.client.ws.SoapItemWebService_Service;
import org.uddi.api_v3.*;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;

public class JuddiClient {
    private static final String COMPANY_NAME = "SomeBigCompanyName";
    public static void main(String[] args) throws TransportException, ConfigurationException, RemoteException {
        if (args.length != 2 || (!args[0].equals("search") && !args[0].equals("register"))) {
            System.err.println("Usage: [register|search] <name to search if search>");
            return;
        }
        UDDIClient client = new UDDIClient("META-INF/juddi.xml");
        Transport transport = client.getTransport("default");
        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIInquiryPortType inquiry = transport.getUDDIInquiryService();
        GetAuthToken getAuthToken = new GetAuthToken();
        getAuthToken.setUserID("admin");
        getAuthToken.setCred("password");
        String token = security.getAuthToken(getAuthToken).getAuthInfo();

        switch (args[0]) {
            case "register":
                System.out.println("Registering service with name '" + args[1]+"' in company '"+COMPANY_NAME+"'");
                registerService(transport.getUDDIPublishService(), token, COMPANY_NAME, args[1], "http://localhost:8080/itemService?wsdl");
                System.out.println("Service was successfully registered");
                break;
            case "search":
                System.out.println("Looking for service with name '" + args[1]+"'");
                String accessPoint = findServiceAccessPoint(inquiry, token, args[1]);
                if (accessPoint == null) {
                    System.err.println("Entrypoint for given service name is not found");
                    return;
                }
                System.out.println("Found service entrypoint: " + accessPoint);
                System.out.println("Trying to reach...");
                try {
                    SoapItemWebService_Service service = new SoapItemWebService_Service(new URL(accessPoint));
                    System.out.println("Successfully reached. Listing objects...");
                    service.getSoapItemWebServicePort().getItems().forEach(i -> System.out.println(i.getName() + ", (" + i.getPower() + "," + i.getLevel()+ "," + i.getPrice()+ ", " + i.getDescription() + ")"));
                } catch (MalformedURLException | ServiceException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

        security.discardAuthToken(new DiscardAuthToken(token));
    }

    private static void registerService(
            UDDIPublicationPortType publish,
            String token,
            String businessName,
            String registeredServiceName,
            String registeredServiceURL) throws RemoteException {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.getName().add(new Name(businessName, "en"));

        SaveBusiness saveBusinessOperation = new SaveBusiness();
        saveBusinessOperation.getBusinessEntity().add(businessEntity);
        saveBusinessOperation.setAuthInfo(token);

        BusinessDetail bd = publish.saveBusiness(saveBusinessOperation);
        String businessKey = bd.getBusinessEntity().get(0).getBusinessKey();
        BusinessService myService = new BusinessService();
        myService.setBusinessKey(businessKey);
        myService.getName().add(new Name(registeredServiceName, "en"));
        BindingTemplate bindingTemplate = new BindingTemplate();
        bindingTemplate.setAccessPoint(new AccessPoint(registeredServiceURL, AccessPointType.WSDL_DEPLOYMENT.toString()));
        BindingTemplates bindingTemplates = new BindingTemplates();
        bindingTemplate = UDDIClient.addSOAPtModels(bindingTemplate);
        bindingTemplates.getBindingTemplate().add(bindingTemplate);
        myService.setBindingTemplates(bindingTemplates);
        SaveService saveServiceOperation = new SaveService();
        saveServiceOperation.getBusinessService().add(myService);
        saveServiceOperation.setAuthInfo(token);
        publish.saveService(saveServiceOperation);
    }

    private static String findServiceAccessPoint(
            UDDIInquiryPortType inquiry,
            String token,
            String serviceName) throws RemoteException {

        FindBusiness fb = new FindBusiness();
        fb.setAuthInfo(token);
        FindQualifiers fq = new FindQualifiers();
        fq.getFindQualifier().add(UDDIConstants.APPROXIMATE_MATCH);
        fb.setFindQualifiers(fq);
        fb.getName().add(new Name(UDDIConstants.WILDCARD, "en"));
        BusinessInfos infos = inquiry.findBusiness(fb).getBusinessInfos();

        for (int i = 0; i < infos.getBusinessInfo().size(); i++) {
            GetServiceDetail gsd = new GetServiceDetail();
            for (int j = 0; j < infos.getBusinessInfo().get(i).getServiceInfos().getServiceInfo().size(); j++) {
                gsd.getServiceKey().add(infos.getBusinessInfo().get(i).getServiceInfos().getServiceInfo().get(j).getServiceKey());
            }
            gsd.setAuthInfo(token);
            ServiceDetail serviceDetail = inquiry.getServiceDetail(gsd);
            for (int j = 0; j < serviceDetail.getBusinessService().size(); j++) {
                BusinessService get = serviceDetail.getBusinessService().get(j);
                Optional<Name> name = get.getName().stream().filter(n -> n.getValue().equals(serviceName)).findFirst();

                if (name.isPresent()) {
                    for (int k = 0; k < get.getBindingTemplates().getBindingTemplate().size(); k++) {
                        AccessPoint accessPoint = get.getBindingTemplates().getBindingTemplate().get(k).getAccessPoint();
                        if (accessPoint != null && accessPoint.getUseType() != null && accessPoint.getUseType().equalsIgnoreCase(AccessPointType.WSDL_DEPLOYMENT.toString())) {
                            return accessPoint.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }
}
