package com.harsha.blob;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
@Controller
public class UploadImageServlet {

	@RequestMapping("/")
	public String home()
	{
		return "index";
	}
	
	@RequestMapping("/upload")
	public void upload(HttpServletRequest req,HttpServletResponse res)
	{
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {
            try {
				res.sendRedirect("/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
				res.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	@RequestMapping("/serve")
	public void serve(HttpServletRequest req,HttpServletResponse res)
	{
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        try {
			blobstoreService.serve(blobKey, res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
