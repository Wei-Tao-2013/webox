package com.webox.quote.process;

import com.webox.common.model.AppMessage;
import com.webox.common.model.Job;
import com.webox.common.model.Quote;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.Service;
import com.webox.common.process.JobManage;
import com.webox.common.process.QuoteManage;
import com.webox.common.process.ServiceManage;
import com.webox.common.utils.AppConsts;
import com.webox.quote.service.QuoteAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QuoteImpl implements QuoteAPI {

	private static final Logger logger = LoggerFactory.getLogger(QuoteImpl.class);

	@Autowired
	@Qualifier("quoteManage")
	private QuoteManage quoteManage;

	@Autowired
	@Qualifier("jobManage")
	private JobManage jobManage;

	@Autowired
	@Qualifier("generalServiceManage")
	private ServiceManage serviceManage;

	private AppConsts appConts = new AppConsts();
	private AppMessage appMessage = new AppMessage();
   /*
	@Override
	public Response searchServices(Request request) {
		Response response = new Response();
		response.setServices(serviceManage.searchServicesByTag());
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}
	*/

	@Override
	public Response postAJob(Request request) {
		Response response = new Response();
		this.appMessage = jobManage.postAJob(request);
		response.setAppInfo(this.appMessage.getMessage());
		response.setJobId(this.appMessage.getDataReturn());
		response.setAppStatus(AppConsts.RETURN_TRUE);
		response.setAppMessage(this.appMessage);
		return response;
	}

	@Override
	public Response postAJobAndAQuote(Request request) { // Job object, serviceId
		Response response = new Response();
		this.appMessage = jobManage.postAJob(request); // need transcation check
		String jobId = this.appMessage.getDataReturn();
		if (jobId != null) {
			Service service = serviceManage.loadAService(request.getServiceId()).getService();
			int servcieOrderProcess = service.getServiceOrderProcess();
			Job job = jobManage.loadAJob(jobId);
			response.setJobId(jobId);
			response.setQuoteId(quoteManage.sendAQuote(job, service));
			this.appMessage.setMessage(AppConsts.QUOTE_SENT + "[QUOTE ID]");
			this.appMessage.setDataReturn(response.getQuoteId());
			this.appMessage.setMsgType("OK");
			this.appMessage.setMsgId("quote-07");
			response.setAppMessage(this.appMessage);
			response.setAppStatus(AppConsts.RETURN_TRUE);
			// check service order process
			if (servcieOrderProcess == 1) { // order auto booked
				Quote quote = quoteManage.loadAQuote(response.getQuoteId());
				request.setQuoteId(response.getQuoteId());
				this.appMessage =  quoteManage.quotePriceSent(quote);
				response.setQuoteId(quote.getQuoteId());
				response.setAppMessage(this.appMessage);
				if ("OK".equalsIgnoreCase(this.appMessage.getMsgType())){
					response = this.quoteAccept(request);
				}
				
			}
		} else {
			this.appMessage.setMessage("Quote Failed Sent Out As Missing Job ");
			this.appMessage.setDataReturn(null);
			this.appMessage.setMsgType("ERR");
			this.appMessage.setMsgId("quote-err-08");
			response.setAppMessage(this.appMessage);
		}
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response sendAQuote(Request request) {
		Response response = new Response();
		Service service = serviceManage.loadAService(request.getServiceId()).getService();
		int servcieOrderProcess = service.getServiceOrderProcess();
		Job job = jobManage.loadAJob(request.getJobId());
		response.setQuoteId(quoteManage.sendAQuote(job, service));
		this.appMessage.setMessage(AppConsts.QUOTE_SENT + "[QUOTE ID]");
		this.appMessage.setDataReturn(response.getQuoteId());
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("quote-07");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		if (servcieOrderProcess == 1) { // order auto booked
			Quote quote = quoteManage.loadAQuote(response.getQuoteId());
			request.setQuoteId(response.getQuoteId());
			this.appMessage =  quoteManage.quotePriceSent(quote);
			response.setQuoteId(quote.getQuoteId());
			response.setAppMessage(this.appMessage);
			if ("OK".equalsIgnoreCase(this.appMessage.getMsgType())){
				response = this.quoteAccept(request);
			}
		}
		return response;
	}

	@Override
	public Response loadAJob(Request request) {
		Response response = new Response();
		response.setJob(jobManage.loadAJob(request.getJobId()));
		this.appMessage.setMessage(AppConsts.JOBLIST_LOADED + "[JOB ID]");
		this.appMessage.setDataReturn(response.getJob().getJobId());
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("job-06");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadJobListByUser(Request request) {
		Response response = new Response();
		response.setJobList(jobManage.loadJobListByUser(request.getUserId(),
				appConts.getJobStatusHashMap().get(request.getJobStatus())));
		this.appMessage.setMessage(AppConsts.JOBLIST_LOADED + "[JOB SIZE]");
		this.appMessage.setDataReturn(response.getJobList().size() + "");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("job-05");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadQuotesByService(Request request) {
		Response response = new Response();
		response.setQuoteList(quoteManage.loadQuotesByService(request.getServiceId()));
		this.appMessage.setMessage(AppConsts.QUOTES_LOADED + "[QUOTE SIZE]");
		this.appMessage.setDataReturn(response.getQuoteList().size() + "");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("quote-05");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadQuotesByUser(Request request) {
		Response response = new Response();
		response.setQuoteGroup(quoteManage.loadQuotesByUser(request.getUserId()));
		this.appMessage.setMessage(AppConsts.QUOTES_LOADED + "[QUOTE SIZE]");
		this.appMessage.setDataReturn(response.getQuoteGroup().size() + "");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("quote-05");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadQuotesByServiceAndUser(Request request) {
		Response response = new Response();
		response.setQuoteList(quoteManage.loadQuotesByServiceAndUser(request.getServiceId(), request.getUserId()));
		this.appMessage.setMessage(AppConsts.QUOTES_LOADED + "[QUOTE SIZE]");
		this.appMessage.setDataReturn(response.getQuoteList().size() + "");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("quote-05");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response loadQuotesByVendor(Request request) {
		Response response = new Response();
		response.setQuoteGroup(quoteManage.loadQuotesByVendor(request.getVendorId()));
		this.appMessage.setMessage(AppConsts.QUOTES_LOADED + "[QUOTE SIZE]");
		this.appMessage.setDataReturn(response.getQuoteGroup().size() + "");
		this.appMessage.setMsgType("OK");
		this.appMessage.setMsgId("quote-05");
		response.setAppMessage(this.appMessage);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response quotePriceSent(Request request) {
		Response response = new Response();
		Quote quote = quoteManage.loadAQuote(request.getQuoteId());

		if (quote != null) {
			quote.setPriceDesc(request.getPriceDesc());
			quote.setQuoteReply(request.getQuoteReply());
			this.appMessage = quoteManage.quotePriceSent(quote);
			response.setQuoteId(quote.getQuoteId());
			response.setAppMessage(this.appMessage);
			Service service = serviceManage.loadAService(quote.getServiceId()).getService();
			if ("OK".equalsIgnoreCase(this.appMessage.getMsgType())){
				if (service.getServiceOrderProcess() == 2) { // vendor auto accpeted
					quote = quoteManage.loadAQuote(request.getQuoteId());
					request.setQuote(quote);
					response = this.quoteAccept(request);
				}
			}
		} else {
			this.appMessage.setMessage(AppConsts.QUOTE_NOT_FOUND + "[QUOTE ID]");
			this.appMessage.setDataReturn(request.getQuoteId());
			this.appMessage.setMsgType("ERR");
			this.appMessage.setMsgId("quote-err-06");
			response.setAppMessage(this.appMessage);
		}
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response quoteAccept(Request request) {
		Response response = new Response();
		Quote quote = quoteManage.loadAQuote(request.getQuoteId());
		if (quote != null) {
			this.appMessage = quoteManage.quoteAccept(quote);
			response.setQuoteId(quote.getQuoteId());
			response.setOrderId(this.appMessage.getDataReturn());
			response.setAppMessage(this.appMessage);
		} else {
			this.appMessage.setMessage(AppConsts.QUOTE_NOT_FOUND + "[QUOTE ID]");
			this.appMessage.setDataReturn(request.getQuoteId());
			this.appMessage.setMsgType("ERR");
			this.appMessage.setMsgId("quote-err-06");
			response.setAppMessage(this.appMessage);
		}
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	@Override
	public Response quoteRejected(Request request) {
		Response response = new Response();
		Quote quote = quoteManage.loadAQuote(request.getQuoteId());
		if (quote != null) {
			this.appMessage = quoteManage.quoteRejected(quote);
			response.setQuoteId(quote.getQuoteId());
			response.setAppMessage(this.appMessage);
		} else {
			this.appMessage.setMessage(AppConsts.QUOTE_NOT_FOUND + "[QUOTE ID]");
			this.appMessage.setDataReturn(request.getQuoteId());
			this.appMessage.setMsgType("ERR");
			this.appMessage.setMsgId("quote-err-06");
			response.setAppMessage(this.appMessage);
		}
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}
}