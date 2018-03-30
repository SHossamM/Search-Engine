CREATE TABLE [dbo].[SeedSet](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[url] [varchar](900) NOT NULL,
	[visited] [datetime] NOT NULL,
	[parseable] [int] NOT NULL,
	[verified] [int] NOT NULL,
	[robotallowed] [int] NOT NULL,
	[HashContent] [int] NULL,
	[ToBeIndexed] [int] NOT NULL,
	[Rank] [int] NOT NULL,
 CONSTRAINT [PK_SeedSet] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_visited]  DEFAULT ((1980-01-01)) FOR [visited]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_parsable]  DEFAULT ((1)) FOR [parseable]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_verified]  DEFAULT ((1)) FOR [verified]
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_robotallowed]  DEFAULT ((1)) FOR [robotallowed]
GO
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_ToBeIndexed]  DEFAULT ((0)) FOR [ToBeIndexed]
GO
GO
ALTER TABLE [dbo].[SeedSet] ADD  CONSTRAINT [DF_SeedSet_Rank]  DEFAULT ((0)) FOR [Rank]
GO



