package uber.com.myapplication;

import org.junit.Assert;
import org.junit.Test;

import uber.com.myapplication.models.PhotoResponse;
import uber.com.myapplication.rest.JSONParser;
import uber.com.myapplication.rest.RequestProcessor;

/**
 * Test case to test whether Search result is fetched and parsed
 * from Network
 */
public class SearchText {

    @Test
      public void testSearch(){
        RequestProcessor requestProcessor =  new RequestProcessor();
        try {
                String response = requestProcessor.getResponse("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=kittens");
                if(response==null){
                    Assert.fail("Could not get Result");
                }
                else{
                       try {
                           PhotoResponse photoResponse = (new JSONParser()).parsePhotoResponse(response);
                           if (photoResponse.getPhotos() !=  null && photoResponse.getPhotos().size() > 5) {
                               Assert.assertTrue(true);
                           }
                       }catch (Exception ex){
                           Assert.fail("Exception in parsing the Response "+ex.getMessage());
                       }

                }
        }
        catch (Exception ex){
            Assert.fail("Exception in getting Response "+ex.getMessage());
        }

    }

}
