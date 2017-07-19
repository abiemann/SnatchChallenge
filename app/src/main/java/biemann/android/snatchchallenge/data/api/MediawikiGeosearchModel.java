package biemann.android.snatchchallenge.data.api;


/*
Will contain the data in response to the POST by the API

RESPONSE FORMATTING
{
	"batchcomplete": "",
	"query": {
		"geosearch": [{
			"pageid": 15481844,
			"ns": 0,
			"title": "Sea Containers House",
			"lat": 51.5085,
			"lon": -0.107,
			"dist": 50.4,
			"primary": ""
		}, {
			...
		}]
	}
}
*/


import java.util.List;

public class MediawikiGeosearchModel
{
    private final String batchcomplete;
    private final Query query;

    public MediawikiGeosearchModel(String batchcomplete, Query query)
    {
        this.batchcomplete = batchcomplete;
        this.query = query;
    }

    public String getBatchcomplete()
    {
        return batchcomplete;
    }

    public Query getQuery()
    {
        return query;
    }

    class Query
    {
        List<QueryListItem> geosearch;

        public List<QueryListItem> getGeosearch()
        {
            return geosearch;
        }
    }

    class QueryListItem
    {
        Long pageid;
        Integer ns;
        String title;
        Double lat;
        Double lon;
        Double dist;
        String primary;

        public Long getPageid()
        {
            return pageid;
        }

        public Integer getNs()
        {
            return ns;
        }

        public String getTitle()
        {
            return title;
        }

        public Double getLat()
        {
            return lat;
        }

        public Double getLon()
        {
            return lon;
        }

        public Double getDist()
        {
            return dist;
        }

        public String getPrimary()
        {
            return primary;
        }
    }
}
